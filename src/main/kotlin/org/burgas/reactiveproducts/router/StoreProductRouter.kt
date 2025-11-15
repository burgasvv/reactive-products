package org.burgas.reactiveproducts.router

import org.burgas.reactiveproducts.entity.store.StoreProduct
import org.burgas.reactiveproducts.router.contract.Router
import org.burgas.reactiveproducts.service.StoreProductService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.router

@Configuration
class StoreProductRouter(override val service: StoreProductService) : Router<StoreProductService> {

    @Bean
    fun storeProductRoutes() = router {

        POST("/api/v1/store-product/add-products") {
            ServerResponse.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                    service.addProducts(it.bodyToFlux(StoreProduct::class.java)),
                    Void::class.java
                )
        }

        PUT("/api/v1/store-product/change-amount") {
            ServerResponse.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                    service.changeAmount(it.bodyToMono(StoreProduct::class.java)),
                    Void::class.java
                )
        }

        DELETE("/api/v1/store-product/remove-products") {
            ServerResponse.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                    service.removeProducts(it.bodyToFlux(StoreProduct::class.java)),
                    Void::class.java
                )
        }
    }
}