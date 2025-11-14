package org.burgas.reactiveproducts.mapper.contract

import org.burgas.reactiveproducts.dto.Request
import org.burgas.reactiveproducts.dto.Response
import org.burgas.reactiveproducts.entity.Entity
import reactor.core.publisher.Mono

interface FullMapper<R : Request, E : Entity, S : Response, F : Response> : EntityMapper<R, E> {

    override fun toEntityCreate(requestMono: Mono<R>): Mono<E>

    override fun toEntityUpdate(requestMono: Mono<R>): Mono<E>

    fun toShortResponse(entity: Mono<E>): Mono<S>

    fun toFullResponse(entity: Mono<E>): Mono<F>
}