package org.burgas.reactiveproducts.router

import org.burgas.reactiveproducts.dto.store.StoreFullResponse
import org.burgas.reactiveproducts.dto.store.StoreRequest
import org.burgas.reactiveproducts.dto.store.StoreShortResponse
import org.burgas.reactiveproducts.router.contract.Router
import org.burgas.reactiveproducts.service.StoreService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.router
import java.util.UUID

@Configuration
class StoreRouter(override val service: StoreService) : Router<StoreService> {

    @Bean
    fun storeRoutes() = router {

        GET("/api/v1/stores") {
            ServerResponse.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(service.findAll(), StoreShortResponse::class.java)
        }

        GET("/api/v1/stores/by-id") {
            ServerResponse.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                    service.findById(UUID.fromString(it.queryParam("storeId").orElseThrow())),
                    StoreFullResponse::class.java
                )
        }

        POST("/api/v1/stores/create") {
            ServerResponse.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                    service.create(it.bodyToMono(StoreRequest::class.java)),
                    Void::class.java
                )
        }

        PUT("/api/v1/stores/update") {
            ServerResponse.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                    service.update(it.bodyToMono(StoreRequest::class.java)),
                    Void::class.java
                )
        }

        DELETE("/api/v1/stores/delete") {
            ServerResponse.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                    service.delete(UUID.fromString(it.queryParam("storeId").orElseThrow())),
                    Void::class.java
                )
        }
    }
}