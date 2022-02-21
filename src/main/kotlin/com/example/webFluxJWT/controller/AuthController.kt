package com.example.webFluxJWT.controller

import com.example.webFluxJWT.configuration.JWTUtil
import com.example.webFluxJWT.model.AuthRequest
import com.example.webFluxJWT.model.AuthResponse
import com.example.webFluxJWT.model.RefreshRequest
import com.example.webFluxJWT.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController


@RestController
class AuthController(
    private val jwtUtil: JWTUtil,
    private val passwordEncoder: PasswordEncoder,
    private val userService: UserService
) {

    @PostMapping("/login")
    suspend fun login(@RequestBody authRequest: AuthRequest): ResponseEntity<Any> {

        val user = userService.findUserByUsername(authRequest.username)

        return if(user != null) {

            val doubleCheckPassword = passwordEncoder.matches(authRequest.password, user.password)

            if (!doubleCheckPassword)
                ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
            else
                // TODO("update our backend DB -> backend should store client's tokens")
                // client at any time should own only 2 (access+refresh) valid tokens
                ResponseEntity.ok(AuthResponse(jwtUtil.generateTokens(user)))
        }else{
            ResponseEntity.badRequest().body("username and/or password not specified")
        }
    }

    @PostMapping("/refresh")
    suspend fun refreshClientsTokens(@RequestBody request: RefreshRequest): ResponseEntity<Any> {

        val user = userService.findUserByUsername(jwtUtil.getUsernameFromToken(request.token))

        return if(user != null) {
            // TODO("update our backend DB -> replace client's old tokens with new tokens")
            // so that client at any time owns only 2 (access+refresh) valid tokens
            ResponseEntity.ok(AuthResponse(jwtUtil.generateTokens(user)))
        }else{
            ResponseEntity.badRequest().build()
        }
    }

}