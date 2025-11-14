package org.burgas.reactiveproducts.service.contract

import org.burgas.reactiveproducts.dto.Request
import org.burgas.reactiveproducts.dto.Response
import org.burgas.reactiveproducts.entity.Entity
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Component
interface CrudService<I, R : Request, E : Entity, S : Response, F : Response> {

    fun findAll(): Flux<S>

    fun findById(id: I): Mono<F>

    fun findEntity(id: I): Mono<E>

    fun create(entityRequestMono: Mono<R>): Mono<Void>

    fun update(entityRequestMono: Mono<R>): Mono<Void>

    fun delete(id: I): Mono<Void>
}