package org.burgas.reactiveproducts.repository

import org.burgas.reactiveproducts.entity.store.StoreProduct
import org.burgas.reactiveproducts.entity.store.StoreProductPK
import org.springframework.data.r2dbc.repository.Modifying
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.r2dbc.repository.R2dbcRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.*

@Repository
interface StoreProductRepository : R2dbcRepository<StoreProduct, UUID> {

    fun findStoreProductByStoreIdAndProductId(
        storeId: UUID,
        productId: UUID
    ): Mono<StoreProduct>

    fun deleteStoreProductByStoreIdAndProductId(storeId: UUID, productId: UUID): Mono<Void>

    @Modifying
    @Query(value = "update store_product set amount = :amount where store_id = :storeId and product_id = :productId")
    fun updateStoreProductByStoreIdAndProductId(storeId: UUID, productId: UUID, amount: Long): Mono<Void>
}