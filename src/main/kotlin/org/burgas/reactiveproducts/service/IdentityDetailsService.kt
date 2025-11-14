package org.burgas.reactiveproducts.service

import org.burgas.reactiveproducts.entity.identity.IdentityDetails
import org.burgas.reactiveproducts.repository.IdentityRepository
import org.springframework.security.core.userdetails.ReactiveUserDetailsService
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Mono

@Service
@Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
class IdentityDetailsService : ReactiveUserDetailsService {

    private final val identityRepository: IdentityRepository

    constructor(identityRepository: IdentityRepository) {
        this.identityRepository = identityRepository
    }

    override fun findByUsername(username: String?): Mono<UserDetails?>? {
        return this.identityRepository.findIdentityByEmail(username ?: throw IllegalArgumentException("Email is null"))
            .map { identity -> IdentityDetails(identity) }
    }
}