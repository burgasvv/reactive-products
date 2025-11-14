package org.burgas.reactiveproducts.entity.product

import org.burgas.reactiveproducts.entity.Entity
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime
import java.util.*

@Table(name = "product", schema = "public")
class Product : Entity {

    @Id
    @Column(value = "id")
    override lateinit var id: UUID

    @Column(value = "category_id")
    lateinit var categoryId: UUID

    @Column(value = "name")
    lateinit var name: String

    @Column(value = "description")
    lateinit var description: String

    @Column(value = "price")
    var price: Double = 0.0

    @Column(value = "created_at")
    lateinit var createdAt: LocalDateTime

    @Column(value = "updated_at")
    lateinit var updatedAt: LocalDateTime

    constructor()
}