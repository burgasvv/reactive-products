package org.burgas.reactiveproducts.dto.category

import org.burgas.reactiveproducts.dto.Request
import java.util.UUID

data class CategoryRequest(
    override var id: UUID?,
    val name: String?,
    val description: String?
) : Request
