package org.burgas.reactiveproducts.mapper

import org.burgas.reactiveproducts.dto.store.StoreFullResponse
import org.burgas.reactiveproducts.dto.store.StoreRequest
import org.burgas.reactiveproducts.dto.store.StoreShortResponse
import org.burgas.reactiveproducts.dto.store.StoreWithAmountResponse
import org.burgas.reactiveproducts.entity.store.Store
import org.burgas.reactiveproducts.mapper.contract.FullMapper
import org.burgas.reactiveproducts.repository.StoreProductRepository
import org.burgas.reactiveproducts.repository.StoreRepository
import org.springframework.beans.factory.ObjectFactory
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID

@Component
class StoreMapper : FullMapper<StoreRequest, Store, StoreShortResponse, StoreFullResponse> {

    final val storeRepository: StoreRepository
    private final val productMapperObjectFactory: ObjectFactory<ProductMapper>
    private final val storeProductRepositoryObjectFactory: ObjectFactory<StoreProductRepository>

    constructor(
        storeRepository: StoreRepository,
        productMapperObjectFactory: ObjectFactory<ProductMapper>,
        storeProductRepositoryObjectFactory: ObjectFactory<StoreProductRepository>
    ) {
        this.storeRepository = storeRepository
        this.productMapperObjectFactory = productMapperObjectFactory
        this.storeProductRepositoryObjectFactory = storeProductRepositoryObjectFactory
    }

    private fun getProductMapper(): ProductMapper = this.productMapperObjectFactory.`object`

    private fun getStoreProductRepository(): StoreProductRepository = this.storeProductRepositoryObjectFactory.`object`

    override fun toEntityCreate(requestMono: Mono<StoreRequest>): Mono<Store> {
        return requestMono.flatMap { storeRequest ->
            Mono.fromCallable {
                Store().apply {
                    this.name = storeRequest.name ?: throw IllegalArgumentException("Name is null")
                    this.address = storeRequest.address ?: throw IllegalArgumentException("Address is null")
                    this.createdAt = LocalDateTime.now()
                    this.updatedAt = LocalDateTime.now()
                }
            }
        }
    }

    override fun toEntityUpdate(requestMono: Mono<StoreRequest>): Mono<Store> {
        return requestMono.flatMap { storeRequest ->
            this.storeRepository.findById(
                storeRequest.id ?: return@flatMap Mono.error { IllegalArgumentException("Store id is null") })
                .flatMap { store ->
                    Mono.fromCallable {
                        Store().apply {
                            this.id = store.id
                            this.name = storeRequest.name ?: store.name
                            this.address = storeRequest.address ?: store.address
                            this.createdAt = store.createdAt
                            this.updatedAt = LocalDateTime.now()
                        }
                    }
                }
        }
    }

    fun timePattern(time: LocalDateTime): String = time.format(
        DateTimeFormatter.ofPattern("dd MMMM yyyy, hh:mm")
    )

    override fun toShortResponse(entity: Mono<Store>): Mono<StoreShortResponse> {
        return entity.flatMap { store ->
            Mono.fromCallable {
                StoreShortResponse(
                    id = store.id,
                    name = store.name,
                    address = store.address,
                    createdAt = timePattern(store.createdAt),
                    updatedAt = timePattern(store.updatedAt)
                )
            }
        }
    }

    override fun toFullResponse(entity: Mono<Store>): Mono<StoreFullResponse> {
        return entity.flatMap { store ->
            this.getProductMapper().productRepository.findProductsByStoreId(store.id)
                .flatMap { product ->
                    this.getProductMapper()
                        .toProductWithAmountResponse(Mono.fromCallable { product }, store.id)
                }
                .collectList()
                .flatMap { productShortResponses ->
                    Mono.fromCallable {
                        StoreFullResponse(
                            id = store.id,
                            name = store.name,
                            address = store.address,
                            createdAt = timePattern(store.createdAt),
                            updatedAt = timePattern(store.updatedAt),
                            products = productShortResponses
                        )
                    }
                }
        }
    }

    fun toStoreWithAmountResponse(entity: Mono<Store>, productId: UUID): Mono<StoreWithAmountResponse> {
        return entity.flatMap { store ->
            this.getStoreProductRepository().findStoreProductByStoreIdAndProductId(store.id, productId)
                .flatMap { storeProduct ->
                    Mono.fromCallable {
                        StoreWithAmountResponse(
                            id = store.id,
                            name = store.name,
                            address = store.address,
                            createdAt = timePattern(store.createdAt),
                            updatedAt = timePattern(store.updatedAt),
                            amount = storeProduct.amount
                        )
                    }
                }
        }
    }
}