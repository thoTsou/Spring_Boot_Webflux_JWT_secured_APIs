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

    fun getAllClaimsFromToken(token: String): Claims =
        Jwts.parserBuilder().setSigningKey(secretKey)
            .build().parseClaimsJws(token).body

    fun getUsernameFromToken(token: String): String =
        getAllClaimsFromToken(token).subject

    fun getExpirationDateFromToken(token: String): Date =
        getAllClaimsFromToken(token).expiration

    fun isTokenExpired(token: String): Boolean =
        getExpirationDateFromToken(token).before(Date())

    fun generateToken(user: User): String {

        val claims: HashMap<String,Any> = hashMapOf()

        claims["role"] = user.roles

        return doGenerateToken(claims,user.username)
    }

    fun validateToken(token: String) =
        !isTokenExpired(token)

    private fun doGenerateToken(claims: Map<String,Any>, username: String): String{
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