package org.burgas.reactiveproducts.entity.store

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Embedded
import org.springframework.data.relational.core.mapping.Table
import java.util.UUID

@Table(name = "store_product", schema = "public")
class StoreProduct {

    @Column(value = "store_id")
    lateinit var storeId: UUID

    @Column(value = "product_id")
    lateinit var productId: UUID

    @Column(value = "amount")
    var amount: Long = 0

    constructor()
}