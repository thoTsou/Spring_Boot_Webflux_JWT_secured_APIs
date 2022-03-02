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
        // if client's handed Bearer token is invalid for some reason
        // jwtUtil.getUsernameFromToken(authToken), will throw an exception
        runCatching {
            val authToken = authentication.credentials.toString()
            val username = jwtUtil.getUsernameFromToken(authToken)

            // token is a valid JWT BUT it might have expired
            if (jwtUtil.hasTokenNotExpired(authToken)) {

                val claims = jwtUtil.getAllClaimsFromToken(authToken)
                val rolesANDprivilagesList = claims["rolesANDprivileges"] as List<String>

                return@mono UsernamePasswordAuthenticationToken(
                    username,
                    null,
                    rolesANDprivilagesList.map { SimpleGrantedAuthority(it) }
                )
            } else {
                // token has expired
                throw Exception("token has expired, so we have to somehow tell client to refresh it")
            }
        }.getOrElse {
            // not a valid JWT
            // API will respond will 401:Unauthorized
            return@mono null
        }
    }
}