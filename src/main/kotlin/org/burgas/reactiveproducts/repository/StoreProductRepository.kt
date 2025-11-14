package org.burgas.reactiveproducts.repository

import org.burgas.reactiveproducts.entity.store.StoreProduct
import org.springframework.data.r2dbc.repository.R2dbcRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono
import java.util.*

@Repository
interface StoreProductRepository : R2dbcRepository<StoreProduct, UUID> {

    fun findStoreProductByStoreIdAndProductId(
        storeId: UUID,
        productId: UUID
    ): Mono<StoreProduct>
}