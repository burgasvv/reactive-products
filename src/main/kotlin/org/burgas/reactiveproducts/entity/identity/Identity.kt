package org.burgas.reactiveproducts.entity.identity

import org.burgas.reactiveproducts.entity.Entity
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime
import java.util.UUID

@Table(name = "identity", schema = "public")
class Identity : Entity {

    @Id
    @Column(value = "id")
    override lateinit var id: UUID

    @Column(value = "authority")
    lateinit var authority: String

    @Column(value = "username")
    lateinit var username: String

    @Column(value = "password")
    lateinit var password: String

    @Column(value = "email")
    lateinit var email: String

    @Column(value = "enabled")
    var enabled: Boolean = true

    @Column(value = "created_at")
    lateinit var createdAt: LocalDateTime

    @Column(value = "updated_at")
    lateinit var updatedAt: LocalDateTime

    constructor()
}