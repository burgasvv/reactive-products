package org.burgas.reactiveproducts.repository

import org.burgas.reactiveproducts.entity.store.Store
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.r2dbc.repository.R2dbcRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import java.util.UUID

@Repository
interface StoreRepository : R2dbcRepository<Store, UUID> {

    @Query(value = "select s.* from store s left join public.store_product sp on s.id = sp.store_id where sp.product_id = :productId")
    fun findStoresByProductId(productId: UUID): Flux<Store>
}