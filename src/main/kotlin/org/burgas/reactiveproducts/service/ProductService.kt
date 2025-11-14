package org.burgas.reactiveproducts.service

import org.burgas.reactiveproducts.dto.product.ProductFullResponse
import org.burgas.reactiveproducts.dto.product.ProductRequest
import org.burgas.reactiveproducts.dto.product.ProductShortResponse
import org.burgas.reactiveproducts.entity.product.Product
import org.burgas.reactiveproducts.mapper.ProductMapper
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
class ProductService : BaseService,
    CrudService<UUID, ProductRequest, Product, ProductShortResponse, ProductFullResponse> {

    private final val productMapper: ProductMapper

    constructor(productMapper: ProductMapper) {
        this.productMapper = productMapper
    }

    override fun findAll(): Flux<ProductShortResponse> {
        return this.productMapper.productRepository.findAll()
            .flatMap { product -> this.productMapper.toShortResponse(Mono.fromCallable { product }) }
    }

    override fun findById(id: UUID): Mono<ProductFullResponse> {
        return this.findEntity(id).flatMap { product -> this.productMapper.toFullResponse(Mono.fromCallable { product }) }
    }

    override fun findEntity(id: UUID): Mono<Product> {
        return this.productMapper.productRepository.findById(id)
    }

    @Transactional(
        isolation = Isolation.READ_COMMITTED,
        propagation = Propagation.REQUIRED,
        rollbackFor = [Throwable::class, RuntimeException::class]
    )
    override fun create(entityRequestMono: Mono<ProductRequest>): Mono<Void> {
        return this.productMapper.toEntityCreate(entityRequestMono)
            .flatMap { product -> this.productMapper.productRepository.save(product) }
            .then()
    }

    @Transactional(
        isolation = Isolation.READ_COMMITTED,
        propagation = Propagation.REQUIRED,
        rollbackFor = [Throwable::class, RuntimeException::class]
    )
    override fun update(entityRequestMono: Mono<ProductRequest>): Mono<Void> {
        return this.productMapper.toEntityUpdate(entityRequestMono)
            .flatMap { product -> this.productMapper.productRepository.save(product) }
            .then()
    }

    @Transactional(
        isolation = Isolation.READ_COMMITTED,
        propagation = Propagation.REQUIRED,
        rollbackFor = [Throwable::class, RuntimeException::class]
    )
    override fun delete(id: UUID): Mono<Void> {
        return this.findEntity(id).flatMap { product -> this.productMapper.productRepository.delete(product) }
    }
}