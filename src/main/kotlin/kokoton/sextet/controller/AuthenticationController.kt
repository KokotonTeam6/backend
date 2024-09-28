package kokoton.sextet.controller

import kokoton.sextet.dto.LoginRequestDTO
import kokoton.sextet.dto.LoginResponseDTO
import kokoton.sextet.dto.RegisterRequestDTO
import kokoton.sextet.model.Profile
import kokoton.sextet.model.ProfileRepository
import kokoton.sextet.util.JwtUtil
import kokoton.sextet.util.hashWithSHA256
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/auth")
class AuthenticationService(
    @Autowired private val jwtUtil: JwtUtil,
    @Autowired private val authenticationManager: AuthenticationManager,
    @Autowired private val profileRepository: ProfileRepository,
    @Autowired private val passwordEncoder: PasswordEncoder,
) {

    @PostMapping("/login")
    fun authenticate(@RequestBody login: LoginRequestDTO): ResponseEntity<LoginResponseDTO> {
        val auth = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(
                login.id,
                hashWithSHA256(login.password)
            )
        )

        if(auth == null) {
            return ResponseEntity(HttpStatus.UNAUTHORIZED)
        }

        val user = profileRepository.findByUsername(login.id)
        val token = jwtUtil.generateToken(user!!, mutableMapOf())

        return ResponseEntity.ok(LoginResponseDTO(login.id, token))
    }

    @PostMapping("/register")
    fun register(@RequestBody register: RegisterRequestDTO): ResponseEntity<String> {
        val hashed = hashWithSHA256(register.password)

        val user = Profile(
            username = register.username,
            password = passwordEncoder.encode(hashed),
            nickname = register.nickname,
        )
        profileRepository.save(user)

        return ResponseEntity.ok("SUCCESS")
    }
}