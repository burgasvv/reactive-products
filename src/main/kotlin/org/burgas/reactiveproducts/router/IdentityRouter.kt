package org.burgas.reactiveproducts.router

import org.burgas.reactiveproducts.dto.identity.IdentityFullResponse
import org.burgas.reactiveproducts.dto.identity.IdentityRequest
import org.burgas.reactiveproducts.dto.identity.IdentityShortResponse
import org.burgas.reactiveproducts.service.IdentityService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyToMono
import org.springframework.web.reactive.function.server.router
import java.util.UUID

@Configuration
class IdentityRouter(override val service: IdentityService) : Router<IdentityService> {

    @Bean
    fun identityRoutes() = router {

        GET("/api/v1/identities") {
            ServerResponse
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(service.findAll(), IdentityShortResponse::class.java)
        }

        GET("/api/v1/identities/by-id") {
            ServerResponse
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                    service.findById(UUID.fromString(it.queryParam("identityId").orElseThrow())),
                    IdentityFullResponse::class.java
                )
        }

        POST("/api/v1/identities/create") {
            ServerResponse
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                    service.create(it.bodyToMono<IdentityRequest>()),
                    Void::class.java
                )
        }

        PUT("/api/v1/identities/update") {
            ServerResponse
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                    service.update(it.bodyToMono<IdentityRequest>()),
                    Void::class.java
                )
        }

        DELETE("/api/v1/identities/delete") {
            ServerResponse
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                    service.delete(UUID.fromString(it.queryParam("identityId").orElseThrow())),
                    Void::class.java
                )
        }

        onError({ true }) { throwable, _ -> ServerResponse.badRequest().bodyValue(throwable.localizedMessage) }
    }
}