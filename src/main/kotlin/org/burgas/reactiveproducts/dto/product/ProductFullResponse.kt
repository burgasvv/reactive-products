package org.burgas.reactiveproducts.dto.product

import org.burgas.reactiveproducts.dto.Response
import org.burgas.reactiveproducts.dto.category.CategoryShortResponse
import org.burgas.reactiveproducts.dto.store.StoreWithAmountResponse
import java.util.*

data class ProductFullResponse(
    override val id: UUID?,
    val category: CategoryShortResponse?,
    val name: String?,
    val description: String?,
    val price: Double?,
    val createdAt: String?,
    val updatedAt: String?,
    val stores: List<StoreWithAmountResponse>?
) : Response
