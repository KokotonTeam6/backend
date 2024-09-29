package kokoton.sextet.controller

import com.aallam.openai.api.chat.ChatCompletionRequest
import com.aallam.openai.api.chat.ChatMessage
import com.aallam.openai.api.chat.ChatRole
import com.aallam.openai.api.logging.LogLevel
import com.aallam.openai.api.model.ModelId
import com.aallam.openai.client.LoggingConfig
import com.aallam.openai.client.OpenAI
import io.github.cdimascio.dotenv.dotenv
import kokoton.sextet.dto.EmailingProfessorRequestDTO
import kokoton.sextet.dto.EmailingProfessorResponseDTO
import kokoton.sextet.model.EmailingProfessor
import kokoton.sextet.model.EmailingProfessorRepository
import kokoton.sextet.util.getCurrentUser
import kotlinx.coroutines.runBlocking
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/mail")
class MailController(
    @Autowired private val emailingProfessorRepository: EmailingProfessorRepository
) {
    private val env = dotenv {
        ignoreIfMissing = true
    }
    private val openai = OpenAI(
        token= env["OPENAI_TOKEN"]!!,
        logging = LoggingConfig(LogLevel.None)
    )

    private val regexp = """최종 점수: ([0-9]+)점/100점""".toRegex(RegexOption.MULTILINE)

    @PostMapping("")
    fun getMailGames(
        @RequestBody request: EmailingProfessorRequestDTO
    ): ResponseEntity<EmailingProfessorResponseDTO> {
        val gptRequest = ChatCompletionRequest(
            model= ModelId("gpt-4o-mini"),
            messages= listOf(
                ChatMessage(
                    ChatRole.System,
                    "당신은 해당 이메일을 받는 교수입니다.\n" +
                    "당신은 교수에게 어떻게 이메일을 쓰면 좋을지 교육하기 위해 학생들에게서 이메일을 받고\n" +
                    "그것을 분석해 100점 만점으로 성적을 매기고자 합니다.\n" +
                    "단 최종 점수의 양식은 `최종 점수: n점/100점` 형식으로 주어져야 합니다.\n" +
                    "다음의 이메일을 확인하십시오."
                ),
                ChatMessage(
                    ChatRole.User,
                    "제목: ${request.title}\n\n" +
                    request.content
                )
            )
        )

        return runBlocking {
            val completion = openai.chatCompletion(gptRequest)
            val response = completion.choices.first().message.content ?: ""

            val score: Short = regexp.find(response)?.groups?.get(1)?.value?.toShort() ?: 0

            val dataset = EmailingProfessor(
                user = getCurrentUser(),
                emailTitle = request.title,
                emailContent = request.content,
                score = score
            )
            emailingProfessorRepository.save(dataset)

            return@runBlocking ResponseEntity.ok(EmailingProfessorResponseDTO(
                request.title,
                request.content,
                score.toInt()
            ))
        }
    }
}