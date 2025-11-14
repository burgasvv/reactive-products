package org.burgas.reactiveproducts.router

import org.burgas.reactiveproducts.dto.category.CategoryFullResponse
import org.burgas.reactiveproducts.dto.category.CategoryRequest
import org.burgas.reactiveproducts.dto.category.CategoryShortResponse
import org.burgas.reactiveproducts.router.contract.Router
import org.burgas.reactiveproducts.service.CategoryService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.router
import java.util.*

@Configuration
class CategoryRouter(override val service: CategoryService) : Router<CategoryService> {

    @Bean
    fun categoryRoutes() = router {

        GET("/api/v1/categories") {
            ServerResponse.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(service.findAll(), CategoryShortResponse::class.java)
        }

        GET("/api/v1/categories/by-id") {
            ServerResponse.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                    service.findById(UUID.fromString(it.queryParam("categoryId").orElseThrow())),
                    CategoryFullResponse::class.java
                )
        }

        POST("/api/v1/categories/create") {
            ServerResponse.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                    service.create(it.bodyToMono(CategoryRequest::class.java)),
                    Void::class.java
                )
        }

        PUT("/api/v1/categories/update") {
            ServerResponse.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                    service.update(it.bodyToMono(CategoryRequest::class.java)),
                    Void::class.java
                )
        }

        DELETE("/api/v1/categories/delete") {
            ServerResponse.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                    service.delete(UUID.fromString(it.queryParam("categoryId").orElseThrow())),
                    Void::class.java
                )
        }
    }
}