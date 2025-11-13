package org.burgas.reactiveproducts.router

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.web.server.csrf.CsrfToken
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.router
import reactor.core.publisher.Mono


@Configuration
class SecurityRouter {

    @Bean
    fun securityRoutes() = router {

        GET("/api/v1/security/csrf-token") { request ->
            val csrfTokenMono: Mono<CsrfToken> = request.exchange()
                .getAttribute(CsrfToken::class.java.getName()) ?: throw IllegalArgumentException("Csrf token is null")
            csrfTokenMono.flatMap { csrfToken -> ServerResponse.ok().bodyValue(csrfToken) }
        }
    }
}