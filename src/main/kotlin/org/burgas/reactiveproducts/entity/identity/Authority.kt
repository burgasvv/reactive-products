package org.burgas.reactiveproducts.entity.identity

import org.springframework.security.core.GrantedAuthority

enum class Authority : GrantedAuthority {

    ADMIN, USER;

    override fun getAuthority(): String? = this.name
}