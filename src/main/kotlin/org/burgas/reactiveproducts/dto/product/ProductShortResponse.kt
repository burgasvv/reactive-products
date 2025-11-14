package org.burgas.reactiveproducts.dto.product

import org.burgas.reactiveproducts.dto.Response
import org.burgas.reactiveproducts.dto.category.CategoryShortResponse
import java.util.UUID

data class ProductShortResponse(
    override val id: UUID?,
    val category: CategoryShortResponse?,
    val name: String?,
    val description: String?,
    val price: Double?,
    val createdAt: String?,
    val updatedAt: String?
) : Response
