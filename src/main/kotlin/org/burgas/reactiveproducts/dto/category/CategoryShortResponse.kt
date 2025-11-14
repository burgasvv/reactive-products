package org.burgas.reactiveproducts.dto.category

import org.burgas.reactiveproducts.dto.Response
import java.util.UUID

data class CategoryShortResponse(
    override val id: UUID?,
    val name: String?,
    val description: String?,
    val createdAt: String?,
    val updatedAt: String?
) : Response
