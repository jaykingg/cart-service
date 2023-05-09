package io.hobbyful.cartService.config

import io.hobbyful.cartService.core.CustomReactiveOpaqueTokenIntrospector
import io.hobbyful.cartService.core.SecurityConstants
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.config.web.server.invoke
import org.springframework.security.oauth2.server.resource.introspection.ReactiveOpaqueTokenIntrospector
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.util.matcher.PathPatternParserServerWebExchangeMatcher
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource

@Configuration
@EnableWebFluxSecurity
class WebFluxSecurityConfig {
    private val corsConfig = CorsConfiguration().apply {
        allowedOrigins = listOf("*")
        allowedHeaders = listOf("*")
        allowedMethods = listOf("GET", "HEAD", "POST", "PUT", "DELETE")
    }

    @Bean
    fun springSecurityFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain {
        return http {
            csrf { disable() }
            securityMatcher(PathPatternParserServerWebExchangeMatcher("/cart/**"))
            authorizeExchange {
                authorize(anyExchange, hasAuthority(SecurityConstants.CUSTOMER))
            }
            oauth2ResourceServer {
                opaqueToken { }
            }
        }
    }

    @Bean
    fun corsConfigurationSource() = UrlBasedCorsConfigurationSource().apply {
        registerCorsConfiguration("/**", corsConfig)
    }

    @Bean
    fun introspector(properties: OAuth2ResourceServerProperties): ReactiveOpaqueTokenIntrospector =
        CustomReactiveOpaqueTokenIntrospector(properties.opaquetoken)
}
