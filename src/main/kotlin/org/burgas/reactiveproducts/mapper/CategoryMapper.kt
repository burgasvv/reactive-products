package org.burgas.reactiveproducts.mapper

import org.burgas.reactiveproducts.dto.category.CategoryFullResponse
import org.burgas.reactiveproducts.dto.category.CategoryRequest
import org.burgas.reactiveproducts.dto.category.CategoryShortResponse
import org.burgas.reactiveproducts.entity.category.Category
import org.burgas.reactiveproducts.mapper.contract.FullMapper
import org.burgas.reactiveproducts.repository.CategoryRepository
import org.springframework.beans.factory.ObjectFactory
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Component
class CategoryMapper : FullMapper<CategoryRequest, Category, CategoryShortResponse, CategoryFullResponse> {

    final val categoryRepository: CategoryRepository
    private final val productMapperObjectFactory: ObjectFactory<ProductMapper>

    constructor(categoryRepository: CategoryRepository, productMapperObjectFactory: ObjectFactory<ProductMapper>) {
        this.categoryRepository = categoryRepository
        this.productMapperObjectFactory = productMapperObjectFactory
    }

    private fun getProductMapper(): ProductMapper = this.productMapperObjectFactory.`object`

    override fun toEntityCreate(requestMono: Mono<CategoryRequest>): Mono<Category> {
        return requestMono.map { categoryRequest ->
            Category().apply {
                this.name = categoryRequest.name ?: throw IllegalArgumentException("Name is null")
                this.description = categoryRequest.description ?: throw IllegalArgumentException("Description is null")
                this.createdAt = LocalDateTime.now()
                this.updatedAt = LocalDateTime.now()
            }
        }
    }

    override fun toEntityUpdate(requestMono: Mono<CategoryRequest>): Mono<Category> {
        return requestMono.flatMap { categoryRequest ->
            this.categoryRepository.findById(categoryRequest.id ?: return@flatMap Mono.error {
                IllegalArgumentException(
                    "Category id is null"
                )
            })
                .map { category ->
                    Category().apply {
                        this.id = category.id
                        this.name = categoryRequest.name ?: category.name
                        this.description = categoryRequest.description ?: category.description
                        this.createdAt = category.createdAt
                        this.updatedAt = LocalDateTime.now()
                    }
                }
        }
    }

    fun timePattern(time: LocalDateTime): String = time.format(
        DateTimeFormatter.ofPattern("dd MMMM yyyy, hh:mm")
    )

    override fun toShortResponse(entity: Mono<Category>): Mono<CategoryShortResponse> {
        return entity.map { category ->
            CategoryShortResponse(
                id = category.id,
                name = category.name,
                description = category.description,
                createdAt = timePattern(category.createdAt),
                updatedAt = timePattern(category.updatedAt)
            )
        }
    }

    override fun toFullResponse(entity: Mono<Category>): Mono<CategoryFullResponse> {
        return entity.flatMap { category ->
            val products = this.getProductMapper().productRepository
                .findProductsByCategoryId(category.id)
                .flatMap { product -> this.getProductMapper().toShortResponse(Mono.fromCallable { product }) }
                .collectList()
            products.flatMap { products ->
                Mono.fromCallable {
                    CategoryFullResponse(
                        id = category.id,
                        name = category.name,
                        description = category.description,
                        createdAt = timePattern(category.createdAt),
                        updatedAt = timePattern(category.updatedAt),
                        products = products
                    )
                }
            }
        }
    }
}