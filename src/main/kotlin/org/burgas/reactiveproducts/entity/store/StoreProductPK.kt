package org.burgas.reactiveproducts.entity.store

import java.io.Serializable
import java.util.*

class StoreProductPK : Serializable {

    var storeId: UUID
    var productId: UUID

    constructor(storeId: UUID, productId: UUID) {
        this.storeId = storeId
        this.productId = productId
    }
}