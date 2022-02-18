package com.example.webFluxJWT

import io.jsonwebtoken.security.Keys
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import javax.crypto.SecretKey

@SpringBootApplication
class WebFluxJwtApplication{

	@Bean
	fun passwordEncoder(): PasswordEncoder =
		BCryptPasswordEncoder(10)

	@Bean
	fun secretKey(): SecretKey =
		Keys.hmacShaKeyFor("secretsecretsecretsecretsecretsecretsecretsecretsecretsecret".toByteArray())

}

fun main(args: Array<String>) {
	runApplication<WebFluxJwtApplication>(*args)
}
