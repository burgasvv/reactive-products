package org.burgas.reactiveproducts.service

import org.burgas.reactiveproducts.dto.category.CategoryFullResponse
import org.burgas.reactiveproducts.dto.category.CategoryRequest
import org.burgas.reactiveproducts.dto.category.CategoryShortResponse
import org.burgas.reactiveproducts.entity.category.Category
import org.burgas.reactiveproducts.mapper.CategoryMapper
import org.burgas.reactiveproducts.service.contract.BaseService
import org.burgas.reactiveproducts.service.contract.CrudService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.UUID

@Service
@Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
class CategoryService : BaseService,
    CrudService<UUID, CategoryRequest, Category, CategoryShortResponse, CategoryFullResponse> {

    private final val categoryMapper: CategoryMapper

    constructor(categoryMapper: CategoryMapper) {
        this.categoryMapper = categoryMapper
    }

    override fun findAll(): Flux<CategoryShortResponse> {
        return this.categoryMapper.categoryRepository.findAll()
            .flatMap { category -> this.categoryMapper.toShortResponse(Mono.fromCallable { category }) }
    }

    override fun findById(id: UUID): Mono<CategoryFullResponse> {
        return this.findEntity(id)
            .flatMap { category -> this.categoryMapper.toFullResponse(Mono.fromCallable { category }) }
            .onErrorMap { IllegalArgumentException("Category not found") }
    }

    override fun findEntity(id: UUID): Mono<Category> {
        return this.categoryMapper.categoryRepository.findById(id)
    }

    @Transactional(
        isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED,
        rollbackFor = [Throwable::class, RuntimeException::class]
    )
    override fun create(entityRequestMono: Mono<CategoryRequest>): Mono<Void> {
        return this.categoryMapper.toEntityCreate(entityRequestMono)
            .flatMap { category -> this.categoryMapper.categoryRepository.save(category) }
            .then()
    }

    @Transactional(
        isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED,
        rollbackFor = [Throwable::class, RuntimeException::class]
    )
    override fun update(entityRequestMono: Mono<CategoryRequest>): Mono<Void> {
        return this.categoryMapper.toEntityUpdate(entityRequestMono)
            .flatMap { category -> this.categoryMapper.categoryRepository.save(category) }
            .then()
    }

    @Transactional(
        isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED,
        rollbackFor = [Throwable::class, RuntimeException::class]
    )
    override fun delete(id: UUID): Mono<Void> {
        return this.findEntity(id).flatMap { category -> this.categoryMapper.categoryRepository.delete(category) }
    }
}