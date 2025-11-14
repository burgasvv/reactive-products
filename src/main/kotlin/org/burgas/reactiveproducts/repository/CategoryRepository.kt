package org.burgas.reactiveproducts.repository

import org.burgas.reactiveproducts.entity.category.Category
import org.springframework.data.r2dbc.repository.R2dbcRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface CategoryRepository : R2dbcRepository<Category, UUID>