package org.burgas.reactiveproducts.dto.category

import org.burgas.reactiveproducts.dto.Response
import org.burgas.reactiveproducts.dto.product.ProductShortResponse
import java.util.UUID

data class CategoryFullResponse(
    override val id: UUID?,
    val name: String?,
    val description: String?,
    val createdAt: String?,
    val updatedAt: String?,
    val products: List<ProductShortResponse>?
) : Response
