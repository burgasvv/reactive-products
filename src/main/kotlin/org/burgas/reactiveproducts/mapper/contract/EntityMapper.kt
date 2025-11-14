package org.burgas.reactiveproducts.mapper.contract

import org.burgas.reactiveproducts.dto.Request
import org.burgas.reactiveproducts.entity.Entity
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
interface EntityMapper<R : Request, E : Entity> {

    fun toEntityCreate(requestMono: Mono<R>): Mono<E>

    fun toEntityUpdate(requestMono: Mono<R>): Mono<E>
}