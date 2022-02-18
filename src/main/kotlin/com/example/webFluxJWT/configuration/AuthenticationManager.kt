package com.example.webFluxJWT.configuration


import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.reactor.mono
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class AuthenticationManager(
    private val jwtUtil: JWTUtil
): ReactiveAuthenticationManager {

    @SuppressWarnings("unchecked")
    override fun authenticate(authentication: Authentication): Mono<Authentication> = mono {

        val authToken = authentication.credentials.toString()
        val username = jwtUtil.getUsernameFromToken(authToken)

        return@mono Mono.just(jwtUtil.validateToken(authToken))
            .filter{valid -> valid}
            .switchIfEmpty(Mono.empty())
            .map {
                val claims = jwtUtil.getAllClaimsFromToken(authToken)
                val rolesList = claims["role"] as List<String>

                UsernamePasswordAuthenticationToken(
                    username,
                    null,
                    rolesList.map { SimpleGrantedAuthority(it) }
                )
            }
            .awaitSingle()
    }
}