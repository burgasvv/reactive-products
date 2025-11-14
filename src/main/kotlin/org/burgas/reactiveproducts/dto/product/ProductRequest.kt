package org.burgas.reactiveproducts.dto.product

import org.burgas.reactiveproducts.dto.Request
import java.util.*

data class ProductRequest(
    override val id: UUID?,
    val categoryId: UUID?,
    val name: String?,
    val description: String?,
    val price: Double?
) : Request
