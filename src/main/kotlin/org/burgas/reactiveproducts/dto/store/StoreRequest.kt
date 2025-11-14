package org.burgas.reactiveproducts.dto.store

import org.burgas.reactiveproducts.dto.Request
import java.util.UUID

data class StoreRequest(
    override val id: UUID?,
    val name: String?,
    val address: String?
) : Request
