package org.burgas.reactiveproducts.router.contract

import org.burgas.reactiveproducts.service.contract.BaseService

interface Router<S : BaseService> {

    val service: S
}