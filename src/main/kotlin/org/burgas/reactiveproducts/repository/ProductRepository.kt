package org.burgas.reactiveproducts.repository

import org.burgas.reactiveproducts.entity.product.Product
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.r2dbc.repository.R2dbcRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import java.util.UUID

@Repository
interface ProductRepository : R2dbcRepository<Product, UUID> {

    fun findProductsByCategoryId(categoryId: UUID): Flux<Product>

    @Query(value = "select p.* from product p left join public.store_product sp on p.id = sp.product_id where sp.store_id = :storeId")
    fun findProductsByStoreId(storeId: UUID): Flux<Product>
}