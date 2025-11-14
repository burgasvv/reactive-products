package org.burgas.reactiveproducts.mapper

import org.burgas.reactiveproducts.dto.identity.IdentityFullResponse
import org.burgas.reactiveproducts.dto.identity.IdentityRequest
import org.burgas.reactiveproducts.dto.identity.IdentityShortResponse
import org.burgas.reactiveproducts.entity.identity.Identity
import org.burgas.reactiveproducts.mapper.contract.FullMapper
import org.burgas.reactiveproducts.repository.IdentityRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Component
class IdentityMapper : FullMapper<IdentityRequest, Identity, IdentityShortResponse, IdentityFullResponse> {

    final val identityRepository: IdentityRepository
    final val passwordEncoder: PasswordEncoder

    constructor(identityRepository: IdentityRepository, passwordEncoder: PasswordEncoder) {
        this.identityRepository = identityRepository
        this.passwordEncoder = passwordEncoder
    }

    override fun toEntityCreate(requestMono: Mono<IdentityRequest>): Mono<Identity> {
        return requestMono.map { identityRequest ->
            Identity().apply {
                this.authority = identityRequest.authority ?: throw IllegalArgumentException("Authority is null")
                this.username = identityRequest.username ?: throw IllegalArgumentException("Username is null")
                this.password = passwordEncoder.encode(identityRequest.password
                    ?: throw IllegalArgumentException("Password is null"))
                this.email = identityRequest.email ?: throw IllegalArgumentException("Email is null")
                this.enabled = identityRequest.enabled ?: throw IllegalArgumentException("Enabled is null")
                this.createdAt = LocalDateTime.now()
                this.updatedAt = LocalDateTime.now()
            }
        }
    }

    override fun toEntityUpdate(requestMono: Mono<IdentityRequest>): Mono<Identity> {
        return requestMono.flatMap { identityRequest ->
            this.identityRepository.findById(
                identityRequest.id ?: return@flatMap Mono.error(IllegalArgumentException("Identity id is null"))
            )
                .map { identity ->
                    Identity().apply {
                        this.id = identity.id
                        this.authority = identityRequest.authority ?: identity.authority
                        this.username = identityRequest.username ?: identity.username
                        this.password = identity.password
                        this.email = identityRequest.email ?: identity.email
                        this.enabled = identityRequest.enabled ?: identity.enabled
                        this.createdAt = identity.createdAt
                        this.updatedAt = LocalDateTime.now()
                    }
                }
        }
    }

    fun timePattern(time: LocalDateTime): String =
        time.format(DateTimeFormatter.ofPattern("dd MMMM yyyy, hh:mm"))

    override fun toShortResponse(entity: Mono<Identity>): Mono<IdentityShortResponse> {
        return entity.flatMap { identity ->
            Mono.fromCallable {
                IdentityShortResponse(
                    id = identity.id,
                    authority = identity.authority,
                    username = identity.username,
                    email = identity.email,
                    createdAt = this.timePattern(identity.createdAt),
                    updatedAt = this.timePattern(identity.updatedAt)
                )
            }
        }
    }

    override fun toFullResponse(entity: Mono<Identity>): Mono<IdentityFullResponse> {
        return entity.flatMap { identity ->
            Mono.fromCallable {
                IdentityFullResponse(
                    id = identity.id,
                    authority = identity.authority,
                    username = identity.username,
                    email = identity.email,
                    createdAt = this.timePattern(identity.createdAt),
                    updatedAt = this.timePattern(identity.updatedAt)
                )
            }
        }
    }
}