package org.burgas.reactiveproducts.dto.identity

import org.burgas.reactiveproducts.dto.Response
import org.burgas.reactiveproducts.entity.identity.Authority
import java.util.UUID

data class IdentityFullResponse(
    override val id: UUID?,
    val authority: String?,
    val username: String?,
    val email: String?,
    val createdAt: String?,
    val updatedAt: String?
) : Response
