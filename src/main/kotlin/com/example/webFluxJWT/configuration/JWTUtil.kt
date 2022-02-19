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

    suspend fun generateToken(user: User): String {

        val claims: HashMap<String,Any> = hashMapOf()

        claims["rolesANDprivileges"] = user.rolesANDprivileges

        return doGenerateToken(claims,user.username)
    }

    suspend fun validateToken(token: String) =
        !isTokenExpired(token)

    private suspend fun doGenerateToken(claims: Map<String,Any>, username: String): String{
        val expTime: Long = 28800 //in second
        val createdDate = Date()
        val expDAte = Date(createdDate.time + expTime * 1000)

        val access_token = Jwts.builder()
            .setClaims(claims)
            .setSubject(username)
            .setIssuedAt(createdDate)
            .setExpiration(expDAte)
            .signWith(secretKey)
            .compact()

        return access_token
    }

}