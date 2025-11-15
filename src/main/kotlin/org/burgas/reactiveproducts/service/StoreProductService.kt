package org.burgas.reactiveproducts.service

import org.burgas.reactiveproducts.entity.store.StoreProduct
import org.burgas.reactiveproducts.repository.ProductRepository
import org.burgas.reactiveproducts.repository.StoreProductRepository
import org.burgas.reactiveproducts.repository.StoreRepository
import org.burgas.reactiveproducts.service.contract.BaseService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kotlin.core.util.function.component1
import reactor.kotlin.core.util.function.component2

@Service
@Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
class StoreProductService : BaseService {

    private final val storeProductRepository: StoreProductRepository
    private final val storeRepository: StoreRepository
    private final val productRepository: ProductRepository

    constructor(
        storeProductRepository: StoreProductRepository,
        storeRepository: StoreRepository,
        productRepository: ProductRepository
    ) {
        this.storeProductRepository = storeProductRepository
        this.storeRepository = storeRepository
        this.productRepository = productRepository
    }


    @Transactional(
        isolation = Isolation.READ_COMMITTED,
        propagation = Propagation.REQUIRED,
        rollbackFor = [Throwable::class, RuntimeException::class]
    )
    fun addProducts(storeProductFlux: Flux<StoreProduct>): Mono<Void> {
        return storeProductFlux.flatMap { storeProduct ->
            Mono.zip(
                this.storeRepository.findById(storeProduct.storeId),
                this.productRepository.findById(storeProduct.productId)
            )
                .flatMap { (store, product) ->
                    if ((store != null && product != null) || (store != null || product != null)) {
                        return@flatMap this.storeProductRepository.save(storeProduct)

                    } else {
                        return@flatMap Mono.error { IllegalArgumentException("Store") }
                    }
                }
        }
            .then()
    }

    @Transactional(
        isolation = Isolation.READ_COMMITTED,
        propagation = Propagation.REQUIRED,
        rollbackFor = [Throwable::class, RuntimeException::class]
    )
    fun removeProducts(storeProductFlux: Flux<StoreProduct>): Mono<Void> {
        return storeProductFlux.flatMap { storeProduct ->
            Mono.zip(
                this.storeRepository.findById(storeProduct.storeId),
                this.productRepository.findById(storeProduct.productId)
            )
                .flatMap { (store, product) ->
                    if ((store != null && product != null) || (store != null || product != null)) {
                        return@flatMap this.storeProductRepository.deleteStoreProductByStoreIdAndProductId(
                            store.id, product.id
                        )

                    } else {
                        return@flatMap Mono.error { IllegalArgumentException("Store or product not found") }
                    }
                }
        }
            .then()
    }

    @Transactional(
        isolation = Isolation.READ_COMMITTED,
        propagation = Propagation.REQUIRED,
        rollbackFor = [Throwable::class, RuntimeException::class]
    )
    fun changeAmount(storeProductMono: Mono<StoreProduct>): Mono<Void> {
        return storeProductMono.flatMap { storeProduct ->
            Mono.zip(
                this.storeRepository.findById(storeProduct.storeId),
                this.productRepository.findById(storeProduct.productId)
            )
                .flatMap { (store, product) ->
                    if ((store != null && product != null) || (store != null || product != null)) {
                        return@flatMap this.storeProductRepository.updateStoreProductByStoreIdAndProductId(
                            store.id, product.id, storeProduct.amount
                        )

                    } else {
                        return@flatMap Mono.error { IllegalArgumentException("Store or product not found") }
                    }
                }
        }
            .then()
    }
}