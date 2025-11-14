package org.burgas.reactiveproducts.entity.category

import org.burgas.reactiveproducts.entity.Entity
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime
import java.util.UUID

@Table(name = "category", schema = "public")
class Category : Entity {

    @Id
    @Column(value = "id")
    override lateinit var id: UUID

    @Column(value = "name")
    lateinit var name: String

    @Column(value = "description")
    lateinit var description: String

    @Column(value = "created_at")
    lateinit var createdAt: LocalDateTime

    @Column(value = "updated_at")
    lateinit var updatedAt: LocalDateTime

    constructor()
}