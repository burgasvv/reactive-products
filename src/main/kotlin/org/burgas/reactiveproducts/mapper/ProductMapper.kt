package org.burgas.reactiveproducts.mapper

import org.burgas.reactiveproducts.dto.product.ProductFullResponse
import org.burgas.reactiveproducts.dto.product.ProductRequest
import org.burgas.reactiveproducts.dto.product.ProductShortResponse
import org.burgas.reactiveproducts.dto.product.ProductWithAmountResponse
import org.burgas.reactiveproducts.entity.product.Product
import org.burgas.reactiveproducts.mapper.contract.FullMapper
import org.burgas.reactiveproducts.repository.ProductRepository
import org.burgas.reactiveproducts.repository.StoreProductRepository
import org.springframework.beans.factory.ObjectFactory
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import reactor.kotlin.core.util.function.component1
import reactor.kotlin.core.util.function.component2
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID

@Component
class ProductMapper : FullMapper<ProductRequest, Product, ProductShortResponse, ProductFullResponse> {

    final val productRepository: ProductRepository
    private final val categoryMapperObjectFactory: ObjectFactory<CategoryMapper>
    private final val storeMapperObjectFactory: ObjectFactory<StoreMapper>
    private final val storeProductRepositoryObjectFactory: ObjectFactory<StoreProductRepository>

    constructor(
        productRepository: ProductRepository,
        categoryMapperObjectFactory: ObjectFactory<CategoryMapper>,
        storeMapperObjectFactory: ObjectFactory<StoreMapper>,
        storeProductRepositoryObjectFactory: ObjectFactory<StoreProductRepository>
    ) {
        this.productRepository = productRepository
        this.categoryMapperObjectFactory = categoryMapperObjectFactory
        this.storeMapperObjectFactory = storeMapperObjectFactory
        this.storeProductRepositoryObjectFactory = storeProductRepositoryObjectFactory
    }

    private fun getCategoryMapper(): CategoryMapper = this.categoryMapperObjectFactory.`object`

    private fun getStoreMapper(): StoreMapper = this.storeMapperObjectFactory.`object`

    private fun getStoreProductRepository(): StoreProductRepository = this.storeProductRepositoryObjectFactory.`object`

    override fun toEntityCreate(requestMono: Mono<ProductRequest>): Mono<Product> {
        return requestMono.flatMap { productRequest ->
            this.getCategoryMapper().categoryRepository.findById(
                productRequest.categoryId
                    ?: return@flatMap Mono.error { IllegalArgumentException("Category id is null") }
            )
                .flatMap { category ->
                    Mono.fromCallable {
                        Product().apply {
                            this.categoryId = category.id
                            this.name = productRequest.name ?: throw IllegalArgumentException("Name is null")
                            this.description =
                                productRequest.description ?: throw IllegalArgumentException("Description is null")
                            this.price = productRequest.price ?: throw IllegalArgumentException("Price is null")
                            this.createdAt = LocalDateTime.now()
                            this.updatedAt = LocalDateTime.now()
                        }
                    }
                }
                .onErrorMap { IllegalArgumentException("Category not found") }
        }
    }

    override fun toEntityUpdate(requestMono: Mono<ProductRequest>): Mono<Product> {
        return requestMono.flatMap { productRequest ->
            this.productRepository.findById(
                productRequest.id ?: return@flatMap Mono.error { IllegalArgumentException("Product id is null") }
            )
                .flatMap { product ->
                    this.getCategoryMapper().categoryRepository.findById(
                        productRequest.categoryId ?: product.categoryId
                    )
                        .flatMap { category ->
                            Mono.fromCallable {
                                Product().apply {
                                    this.id = product.id
                                    this.categoryId = category.id
                                    this.name = productRequest.name ?: product.name
                                    this.description = productRequest.description ?: product.description
                                    this.price = productRequest.price ?: product.price
                                    this.createdAt = product.createdAt
                                    this.updatedAt = LocalDateTime.now()
                                }
                            }
                        }
                }
        }
    }

    fun timePattern(time: LocalDateTime): String = time.format(
        DateTimeFormatter.ofPattern("dd MMMM yyyy, hh:mm")
    )

    override fun toShortResponse(entity: Mono<Product>): Mono<ProductShortResponse> {
        return entity.flatMap { product ->
            this.getCategoryMapper().categoryRepository.findById(product.categoryId)
                .flatMap { category ->
                    this.getCategoryMapper().toShortResponse(Mono.fromCallable { category })
                }
                .flatMap { categoryShortResponse ->
                    Mono.fromCallable {
                        ProductShortResponse(
                            id = product.id,
                            category = categoryShortResponse,
                            name = product.name,
                            description = product.description,
                            price = product.price,
                            createdAt = timePattern(product.createdAt),
                            updatedAt = timePattern(product.updatedAt)
                        )
                    }
                }
        }
    }

    override fun toFullResponse(entity: Mono<Product>): Mono<ProductFullResponse> {
        return entity.flatMap { product ->
            Mono.zip(
                this.getCategoryMapper().categoryRepository.findById(product.categoryId)
                    .flatMap { category -> this.getCategoryMapper().toShortResponse(Mono.fromCallable { category }) },
                this.getStoreMapper().storeRepository.findStoresByProductId(product.id)
                    .flatMap { store ->
                        this.getStoreMapper()
                            .toStoreWithAmountResponse(Mono.fromCallable { store }, product.id)
                    }
                    .collectList()
            )
                .flatMap { (category, stores) ->
                    Mono.fromCallable {
                        ProductFullResponse(
                            id = product.id,
                            category = category,
                            name = product.name,
                            description = product.description,
                            price = product.price,
                            createdAt = timePattern(product.createdAt),
                            updatedAt = timePattern(product.updatedAt),
                            stores = stores
                        )
                    }
                }
        }
    }

    fun toProductWithAmountResponse(entity: Mono<Product>, storeId: UUID): Mono<ProductWithAmountResponse> {
        return entity.flatMap { product ->
            Mono.zip(
                this.getCategoryMapper().categoryRepository.findById(product.categoryId)
                    .flatMap { category -> this.getCategoryMapper().toShortResponse(Mono.fromCallable { category }) },
                this.getStoreProductRepository().findStoreProductByStoreIdAndProductId(storeId, product.id)
            )
                .flatMap { (category, storeProduct) ->
                    Mono.fromCallable {
                        ProductWithAmountResponse(
                            id = product.id,
                            category = category,
                            name = product.name,
                            description = product.description,
                            price = product.price,
                            createdAt = timePattern(product.createdAt),
                            updatedAt = timePattern(product.updatedAt),
                            amount = storeProduct.amount
                        )
                    }
                }
        }
    }
}