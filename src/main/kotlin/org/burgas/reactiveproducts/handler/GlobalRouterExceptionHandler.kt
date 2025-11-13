package org.burgas.reactiveproducts.handler

import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@Component
class GlobalRouterExceptionHandler : ErrorWebExceptionHandler {

    override fun handle(
        exchange: ServerWebExchange,
        ex: Throwable
    ): Mono<Void?> {
        exchange.response.statusCode = HttpStatus.BAD_REQUEST
        return exchange.response.writeWith(
            Mono.fromCallable {
                exchange.response
                    .bufferFactory()
                    .wrap(ex.localizedMessage.toByteArray(Charsets.UTF_8))
            }
        )
    }
}