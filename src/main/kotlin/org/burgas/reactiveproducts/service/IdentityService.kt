package org.burgas.reactiveproducts.service

import org.burgas.reactiveproducts.dto.identity.IdentityFullResponse
import org.burgas.reactiveproducts.dto.identity.IdentityRequest
import org.burgas.reactiveproducts.dto.identity.IdentityShortResponse
import org.burgas.reactiveproducts.entity.identity.Identity
import org.burgas.reactiveproducts.mapper.IdentityMapper
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.*

@Service
@Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
class IdentityService : BaseService,
    CrudService<UUID, IdentityRequest, Identity, IdentityShortResponse, IdentityFullResponse> {

    private final val identityMapper: IdentityMapper

    constructor(identityMapper: IdentityMapper) {
        this.identityMapper = identityMapper
    }

    override fun findAll(): Flux<IdentityShortResponse> {
        return this.identityMapper.identityRepository.findAll()
            .flatMap { identity -> this.identityMapper.toShortResponse(Mono.fromCallable { identity }) }
    }

    override fun findById(id: UUID): Mono<IdentityFullResponse> {
        return this.findEntity(id)
            .flatMap { identity -> this.identityMapper.toFullResponse(Mono.fromCallable { identity }) }
    }

    override fun findEntity(id: UUID): Mono<Identity> {
        return this.identityMapper.identityRepository.findById(id)
            .onErrorMap { throw IllegalArgumentException("Identity not found") }
    }

    @Transactional(
        isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED,
        rollbackFor = [Throwable::class, RuntimeException::class]
    )
    override fun create(entityRequestMono: Mono<IdentityRequest>): Mono<Void> {
        return entityRequestMono.flatMap { identityRequest ->
            this.identityMapper.toEntityCreate(Mono.fromCallable { identityRequest })
        }
            .flatMap { identity -> this.identityMapper.identityRepository.save(identity) }
            .then()
    }

    @Transactional(
        isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED,
        rollbackFor = [Throwable::class, RuntimeException::class]
    )
    override fun update(entityRequestMono: Mono<IdentityRequest>): Mono<Void> {
        return entityRequestMono.flatMap { identityRequest ->
            this.identityMapper.toEntityUpdate(Mono.fromCallable { identityRequest })
        }
            .flatMap { identity -> this.identityMapper.identityRepository.save(identity) }
            .then()
    }

    @Transactional(
        isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED,
        rollbackFor = [Throwable::class, RuntimeException::class]
    )
    override fun delete(id: UUID): Mono<Void> {
        return this.findEntity(id)
            .flatMap { identity -> this.identityMapper.identityRepository.delete(identity) }
    }
}