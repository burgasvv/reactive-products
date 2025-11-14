package org.burgas.reactiveproducts.entity.store

import org.burgas.reactiveproducts.entity.Entity
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime
import java.util.*

@Table(name = "store", schema = "public")
class Store : Entity {

    @Id
    @Column(value = "id")
    override lateinit var id: UUID

    @Column(value = "name")
    lateinit var name: String

    @Column(value = "address")
    lateinit var address: String

    @Column(value = "created_at")
    lateinit var createdAt: LocalDateTime

    @Column(value = "updated_at")
    lateinit var updatedAt: LocalDateTime

    constructor()
}