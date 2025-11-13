package org.burgas.reactiveproducts.converter

import org.burgas.reactiveproducts.entity.identity.Authority
import org.springframework.core.convert.converter.Converter
import org.springframework.data.convert.ReadingConverter
import org.springframework.stereotype.Component

@Component
@ReadingConverter
class AuthorityReadingConverter : Converter<String, Authority> {

    override fun convert(source: String): Authority {
        return Authority.valueOf(source)
    }
}