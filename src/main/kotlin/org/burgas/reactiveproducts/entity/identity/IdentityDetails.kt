package org.burgas.reactiveproducts.entity.identity

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class IdentityDetails : UserDetails {

    val identity: Identity

    constructor(identity: Identity) {
        this.identity = identity
    }

    override fun getAuthorities(): Collection<GrantedAuthority?> {
        return listOf(identity.authority)
    }

    override fun getPassword(): String {
        return identity.password
    }

    override fun getUsername(): String {
        return identity.email
    }

    override fun isEnabled(): Boolean {
        return identity.enabled || !super.isEnabled()
    }
}