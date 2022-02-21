package com.example.webFluxJWT.configuration

import org.springframework.context.annotation.Bean
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.web.server.SecurityWebFilterChain
import reactor.core.publisher.Mono

@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
class WebSecurityConfig(
    private val authenticationManager: AuthenticationManager,
    private val securityContextRepository: SecurityContextRepository
) {

    @Bean
    fun securityWebFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain =
        http
            .exceptionHandling()
            .authenticationEntryPoint { exchange, _ ->
                Mono.fromRunnable{ exchange.response.statusCode = HttpStatus.UNAUTHORIZED }
            }.accessDeniedHandler { exchange, _ ->
                Mono.fromRunnable{ exchange.response.statusCode = HttpStatus.FORBIDDEN }
            }.and()
            // no need for csrf token as this API in not vulnerable to cross site request forgery
            // BECAUSE
            // client MUST hand its API key (JWT) via AUTHORIZATION header
            .csrf().disable()
            .formLogin().disable()
            .httpBasic().disable()
            // XSS protection header (1; mode=block) is added by default
            // BUT we should also
            // use Content Security Policy to overcome XSS
            .headers().contentSecurityPolicy("script-src 'self'").and()
            .and()
            .authenticationManager(authenticationManager)
            .securityContextRepository(securityContextRepository)
            .authorizeExchange()
            .pathMatchers(HttpMethod.OPTIONS).permitAll()
            .pathMatchers("/login").permitAll()
            .anyExchange().authenticated()
            .and().build();

}