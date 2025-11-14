package org.burgas.reactiveproducts.router

import org.burgas.reactiveproducts.dto.product.ProductFullResponse
import org.burgas.reactiveproducts.dto.product.ProductRequest
import org.burgas.reactiveproducts.dto.product.ProductShortResponse
import org.burgas.reactiveproducts.router.contract.Router
import org.burgas.reactiveproducts.service.ProductService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.router
import java.util.UUID

@Configuration
class ProductRouter(override val service: ProductService) : Router<ProductService> {

    @Bean
    fun productRoutes() = router {

        GET("/api/v1/products") {
            ServerResponse.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(service.findAll(), ProductShortResponse::class.java)
        }

        GET("/api/v1/products/by-id") {
            ServerResponse.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                    service.findById(UUID.fromString(it.queryParam("productId").orElseThrow())),
                    ProductFullResponse::class.java
                )
        }

        POST("/api/v1/products/create") {
            ServerResponse.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                    service.create(it.bodyToMono(ProductRequest::class.java)),
                    Void::class.java
                )
        }

        PUT("/api/v1/products/update") {
            ServerResponse.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                    service.update(it.bodyToMono(ProductRequest::class.java)),
                    Void::class.java
                )
        }

        DELETE("/api/v1/products/delete") {
            ServerResponse.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                    service.delete(UUID.fromString(it.queryParam("productId").orElseThrow())),
                    Void::class.java
                )
        }
    }
}