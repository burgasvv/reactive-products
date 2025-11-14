package org.burgas.reactiveproducts.router

import org.burgas.reactiveproducts.dto.identity.IdentityFullResponse
import org.burgas.reactiveproducts.dto.identity.IdentityRequest
import org.burgas.reactiveproducts.dto.identity.IdentityShortResponse
import org.burgas.reactiveproducts.entity.identity.IdentityDetails
import org.burgas.reactiveproducts.router.contract.Router
import org.burgas.reactiveproducts.service.IdentityService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.core.Authentication
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyToMono
import org.springframework.web.reactive.function.server.router
import reactor.core.publisher.Mono
import java.util.*

@Configuration
class IdentityRouter(override val service: IdentityService) : Router<IdentityService> {

    @Bean
    fun identityRoutes() = router {

        filter { request, function ->
            if (
                request.path().equals("/api/v1/identities/by-id", false) ||
                request.path().equals("/api/v1/identities/delete", false) ||
                request.path().equals("/api/v1/identities/change-password", false)
            ) {
                return@filter request.principal().cast(Authentication::class.java)
                    .flatMap { authentication ->
                        if (authentication.isAuthenticated) {
                            val identityDetails = authentication.principal as IdentityDetails
                            val identityId = UUID.fromString(request.queryParam("identityId").orElseThrow())

                            if (identityDetails.identity.id == identityId) {
                                return@flatMap function(request)

                            } else {
                                return@flatMap Mono.error { IllegalArgumentException("Identity not authorized") }
                            }

                        } else {
                            return@flatMap Mono.error { IllegalArgumentException("Identity not authenticated") }
                        }
                    }

            } else if (request.path().equals("/api/v1/identities/update", false)) {
                return@filter request.principal().cast(Authentication::class.java)
                    .flatMap { authentication ->
                        if (authentication.isAuthenticated) {
                            val identityDetails = authentication.principal as IdentityDetails

                            return@flatMap request.bodyToMono(IdentityRequest::class.java)
                                .flatMap { identityRequest ->
                                    if (identityDetails.identity.id == identityRequest.id) {
                                        request.attributes()["identityRequest"] = identityRequest
                                        return@flatMap function(request)

                                    } else {
                                        return@flatMap Mono.error { IllegalArgumentException("Identity not authorized") }
                                    }
                                }

                        } else {
                            return@flatMap Mono.error { IllegalArgumentException("Identity not authenticated") }
                        }
                    }
            }

            return@filter function(request)
        }

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
                    service.update(
                        Mono.fromCallable {
                            it.attribute("identityRequest").orElseThrow() as IdentityRequest
                        }
                    ),
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

        PUT("/api/v1/identities/change-password") {
            ServerResponse
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                    service.changePassword(
                        UUID.fromString(it.queryParam("identityId").orElseThrow()),
                        it.queryParam("newPassword").orElseThrow()
                    ),
                    Void::class.java
                )
        }
    }
}