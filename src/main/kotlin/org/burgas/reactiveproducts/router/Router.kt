package org.burgas.reactiveproducts.router

import org.burgas.reactiveproducts.service.BaseService

interface Router<S : BaseService> {

    val service: S
}