package org.burgas.reactiveproducts.config

import org.burgas.reactiveproducts.entity.identity.Authority
import org.burgas.reactiveproducts.service.CustomUserDetailsService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.csrf.XorServerCsrfTokenRequestAttributeHandler
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource

@Configuration
@EnableWebFluxSecurity
class SecurityConfig {

    private final val passwordEncoder: PasswordEncoder
    private final val customUserDetailsService: CustomUserDetailsService

    constructor(passwordEncoder: PasswordEncoder, customUserDetailsService: CustomUserDetailsService) {
        this.passwordEncoder = passwordEncoder
        this.customUserDetailsService = customUserDetailsService
    }

    @Bean
    fun reactiveAuthenticationManager(): ReactiveAuthenticationManager {
        val authenticationManager = UserDetailsRepositoryReactiveAuthenticationManager(this.customUserDetailsService)
        authenticationManager.setPasswordEncoder(this.passwordEncoder)
        return authenticationManager
    }

    @Bean
    fun securityWebFilterChain(serverHttpSecurity: ServerHttpSecurity): SecurityWebFilterChain {
        return serverHttpSecurity
            .cors { corsSpec -> corsSpec.configurationSource(UrlBasedCorsConfigurationSource()) }
            .csrf { csrfSpec -> csrfSpec.csrfTokenRequestHandler(XorServerCsrfTokenRequestAttributeHandler()) }
            .httpBasic { basicSpec -> basicSpec.authenticationManager(this.reactiveAuthenticationManager()) }
            .authorizeExchange { exchangeSpec ->
                exchangeSpec
                    .pathMatchers(
                        "/api/v1/security/csrf-token",

                        "/api/v1/identities/create"
                    )
                    .permitAll()

                    .pathMatchers(
                        "/api/v1/identities/by-id", "/api/v1/identities/update", "/api/v1/identities/delete",
                        "/api/v1/identities/change-password"
                    )
                    .hasAnyAuthority(Authority.ADMIN.authority, Authority.USER.authority)

                    .pathMatchers(
                        "/api/v1/identities"
                    )
                    .hasAnyAuthority(Authority.ADMIN.authority)
            }
            .build()
    }
}