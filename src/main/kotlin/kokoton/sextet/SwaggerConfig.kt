package kokoton.sextet

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import io.swagger.v3.oas.models.servers.Server
import jakarta.servlet.ServletContext
import jakarta.servlet.http.HttpServletRequest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class SwaggerConfig {
    private val securitySchemeName = "api token"

    @Bean
    fun openAPI(request: ServletContext): OpenAPI {
        return OpenAPI()
            .info(Info().title("코코톤 6팀 Backend RestAPI").version("v1.0.0"))
            .addSecurityItem(SecurityRequirement().addList(securitySchemeName))
            .components(Components()
                .addSecuritySchemes(securitySchemeName,
                    SecurityScheme()
                        .name(securitySchemeName)
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                )
            )
            .servers(listOf(
                Server().url(request.contextPath)
                    .description(request.contextPath)
            ))
    }
}