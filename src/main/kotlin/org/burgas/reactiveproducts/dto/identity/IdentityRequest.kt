package org.burgas.reactiveproducts.dto.identity

import org.burgas.reactiveproducts.dto.Request
import org.burgas.reactiveproducts.entity.identity.Authority
import java.util.UUID

data class IdentityRequest(
    override val id: UUID?,
    val authority: Authority?,
    val username: String?,
    val password: String?,
    val email: String?,
    val enabled: Boolean?
) : Request
