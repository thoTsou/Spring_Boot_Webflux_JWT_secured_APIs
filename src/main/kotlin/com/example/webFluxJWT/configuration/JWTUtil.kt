package com.example.webFluxJWT.configuration

import com.example.webFluxJWT.model.User
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import org.springframework.stereotype.Component
import java.util.Date
import javax.crypto.SecretKey

@Component
class JWTUtil(
    private val secretKey: SecretKey
) {

    suspend fun getAllClaimsFromToken(token: String): Claims =
        Jwts.parserBuilder().setSigningKey(secretKey)
            .build().parseClaimsJws(token).body

    suspend fun getUsernameFromToken(token: String): String =
        getAllClaimsFromToken(token).subject

    suspend fun getExpirationDateFromToken(token: String): Date =
        getAllClaimsFromToken(token).expiration

    suspend fun isTokenExpired(token: String): Boolean =
        getExpirationDateFromToken(token).before(Date())

    suspend fun generateTokens(user: User): Map<String,String> {

        val claims: HashMap<String,Any> = hashMapOf()

        claims["rolesANDprivileges"] = user.rolesANDprivileges

        return doGenerateTokens(claims,user.username)
    }

    suspend fun hasTokenExpired(token: String) =
        !isTokenExpired(token)

    private suspend fun doGenerateTokens(claims: Map<String,Any>, username: String): Map<String,String> {
        val expTime: Long = 28800 //in second
        val createdDate = Date()

        val access_token_expDAte = Date(createdDate.time + expTime * 1000)

        val access_token = Jwts.builder()
            .setClaims(claims)
            .setSubject(username)
            .setIssuedAt(createdDate)
            .setExpiration(access_token_expDAte)
            .signWith(secretKey)
            .compact()

        val refresh_token_expDAte = Date(createdDate.time + expTime * 2000)

        val refresh_token = Jwts.builder()
            .setClaims(claims)
            .setSubject(username)
            .setIssuedAt(createdDate)
            .setExpiration(refresh_token_expDAte)
            .signWith(secretKey)
            .compact()

        return mapOf("access_token" to access_token,"refresh_token" to refresh_token)
    }

}