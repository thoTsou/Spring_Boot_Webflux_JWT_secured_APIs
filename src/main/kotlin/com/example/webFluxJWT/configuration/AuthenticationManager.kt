package com.example.webFluxJWT.configuration


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

    override fun authenticate(authentication: Authentication): Mono<Authentication> = mono {

        val authToken = authentication.credentials.toString()
        val username = jwtUtil.getUsernameFromToken(authToken)

        if (jwtUtil.validateToken(authToken)){

            val claims = jwtUtil.getAllClaimsFromToken(authToken)
            val rolesANDprivilagesList = claims["rolesANDprivileges"] as List<String>

            return@mono UsernamePasswordAuthenticationToken(
                username,
                null,
                rolesANDprivilagesList.map { SimpleGrantedAuthority(it) }
            )
        }else{
            throw Exception("invalid token")
        }
    }
}