package org.burgas.reactiveproducts.dto.store

import org.burgas.reactiveproducts.dto.Response
import java.util.UUID

data class StoreWithAmountResponse(
    override val id: UUID?,
    val name: String?,
    val address: String?,
    val createdAt: String?,
    val updatedAt: String?,
    val amount: Double?
) : Response
