package org.burgas.reactiveproducts.service

import org.burgas.reactiveproducts.dto.store.StoreFullResponse
import org.burgas.reactiveproducts.dto.store.StoreRequest
import org.burgas.reactiveproducts.dto.store.StoreShortResponse
import org.burgas.reactiveproducts.entity.store.Store
import org.burgas.reactiveproducts.mapper.StoreMapper
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
class StoreService : BaseService, CrudService<UUID, StoreRequest, Store, StoreShortResponse, StoreFullResponse> {

    private final val storeMapper: StoreMapper

    constructor(storeMapper: StoreMapper) {
        this.storeMapper = storeMapper
    }

    override fun findAll(): Flux<StoreShortResponse> {
        return this.storeMapper.storeRepository.findAll()
            .flatMap { store -> this.storeMapper.toShortResponse(Mono.fromCallable { store }) }
    }

    override fun findById(id: UUID): Mono<StoreFullResponse> {
        return this.findEntity(id).flatMap { store -> this.storeMapper.toFullResponse(Mono.fromCallable { store }) }
    }

    override fun findEntity(id: UUID): Mono<Store> {
        return this.storeMapper.storeRepository.findById(id)
    }

    @Transactional(
        isolation = Isolation.READ_COMMITTED,
        propagation = Propagation.REQUIRED,
        rollbackFor = [Throwable::class, RuntimeException::class]
    )
    override fun create(entityRequestMono: Mono<StoreRequest>): Mono<Void> {
        return this.storeMapper.toEntityCreate(entityRequestMono)
            .flatMap { store -> this.storeMapper.storeRepository.save(store) }
            .then()
    }

    @Transactional(
        isolation = Isolation.READ_COMMITTED,
        propagation = Propagation.REQUIRED,
        rollbackFor = [Throwable::class, RuntimeException::class]
    )
    override fun update(entityRequestMono: Mono<StoreRequest>): Mono<Void> {
        return this.storeMapper.toEntityUpdate(entityRequestMono)
            .flatMap { store -> this.storeMapper.storeRepository.save(store) }
            .then()
    }

    @Transactional(
        isolation = Isolation.READ_COMMITTED,
        propagation = Propagation.REQUIRED,
        rollbackFor = [Throwable::class, RuntimeException::class]
    )
    override fun delete(id: UUID): Mono<Void> {
        return this.findEntity(id).flatMap { store -> this.storeMapper.storeRepository.delete(store) }
    }
}