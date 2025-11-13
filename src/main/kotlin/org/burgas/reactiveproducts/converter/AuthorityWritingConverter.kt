package org.burgas.reactiveproducts.converter

import org.burgas.reactiveproducts.entity.identity.Authority
import org.springframework.core.convert.converter.Converter
import org.springframework.data.convert.WritingConverter
import org.springframework.stereotype.Component

@Component
@WritingConverter
class AuthorityWritingConverter : Converter<Authority, String> {

    override fun convert(source: Authority): String {
        return source.name
    }
}