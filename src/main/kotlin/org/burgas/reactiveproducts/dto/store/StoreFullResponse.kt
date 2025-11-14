package org.burgas.reactiveproducts.dto.store

import org.burgas.reactiveproducts.dto.Response
import org.burgas.reactiveproducts.dto.product.ProductWithAmountResponse
import java.util.*

data class StoreFullResponse(
    override val id: UUID?,
    val name: String?,
    val address: String?,
    val createdAt: String?,
    val updatedAt: String?,
    val products: List<ProductWithAmountResponse>?
) : Response
