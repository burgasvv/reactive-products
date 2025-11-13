package org.burgas.reactiveproducts

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ReactiveProductsApplication

fun main(args: Array<String>) {
    runApplication<ReactiveProductsApplication>(*args)
}
