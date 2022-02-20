package com.example.webFluxJWT.configuration


import kotlinx.coroutines.reactor.awaitSingleOrNull
import kotlinx.coroutines.reactor.mono
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextImpl
import org.springframework.security.web.server.context.ServerSecurityContextRepository
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono
import org.springframework.http.HttpHeaders

@Component
class SecurityContextRepository(
    private val authenticationManager: AuthenticationManager
): ServerSecurityContextRepository {

    override fun save(exchange: ServerWebExchange, context: SecurityContext): Mono<Void> = mono {
        throw UnsupportedOperationException("Not supported")
    }

    override fun load(exchange: ServerWebExchange): Mono<SecurityContext> = mono {

        val authTokenWithBearerPrefix: String? = exchange.request.headers.getFirst(HttpHeaders.AUTHORIZATION)

        if(authTokenWithBearerPrefix != null && authTokenWithBearerPrefix.startsWith("Bearer ")) {

            val authToken = authTokenWithBearerPrefix.substring(7)
            val auth = UsernamePasswordAuthenticationToken(authToken, authToken)

            // try to authenticate client's handed Bearer token
            return@mono authenticationManager.authenticate(auth).map { SecurityContextImpl(it) }.awaitSingleOrNull()
        }else{
            // AUTHORIZATION HEADER is not specified
            // API will respond with 401:Unauthorized
            return@mono null
        }
    }
}