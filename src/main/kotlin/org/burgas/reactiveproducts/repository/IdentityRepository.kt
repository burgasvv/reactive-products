package org.burgas.reactiveproducts.repository

import org.burgas.reactiveproducts.entity.identity.Identity
import org.springframework.data.r2dbc.repository.R2dbcRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono
import java.util.UUID

@Repository
interface IdentityRepository : R2dbcRepository<Identity, UUID> {

    override fun findById(id: UUID): Mono<Identity>
}