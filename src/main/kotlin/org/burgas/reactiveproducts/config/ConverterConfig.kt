package org.burgas.reactiveproducts.config

import io.r2dbc.spi.ConnectionFactory
import org.burgas.reactiveproducts.converter.AuthorityReadingConverter
import org.burgas.reactiveproducts.converter.AuthorityWritingConverter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.r2dbc.convert.R2dbcCustomConversions
import org.springframework.data.r2dbc.dialect.DialectResolver

@Configuration
class ConverterConfig {

    private val authorityReadingConverter: AuthorityReadingConverter
    private val authorityWritingConverter: AuthorityWritingConverter

    constructor(
        authorityReadingConverter: AuthorityReadingConverter,
        authorityWritingConverter: AuthorityWritingConverter
    ) {
        this.authorityReadingConverter = authorityReadingConverter
        this.authorityWritingConverter = authorityWritingConverter
    }


    @Bean
    fun customConversions(connectionFactory: ConnectionFactory): R2dbcCustomConversions {
        val dialect = DialectResolver.getDialect(connectionFactory)
        return R2dbcCustomConversions.of(
            dialect, listOf(this.authorityReadingConverter, this.authorityWritingConverter)
        )
    }
}