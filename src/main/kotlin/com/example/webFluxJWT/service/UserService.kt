package com.example.webFluxJWT.service

import com.example.webFluxJWT.model.Role
import com.example.webFluxJWT.model.User
import kotlinx.coroutines.reactor.mono
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

/**
 * This is just an example, you can load the user from the database from the repository.
 *
 */
@Service
class UserService(
    private val passwordEncoder: PasswordEncoder,
) {

    suspend fun findUserByUsername(username: String): User? {

        val data: HashMap<String, User> = HashMap()

        //username:password -> user:user
        data["user"] = User(
            username = "user",
            password = passwordEncoder.encode("user"),
            rolesANDprivileges = listOf("ROLE_USER","user:read") )

        //username:password -> admin:admin
        data["admin"] = User(
            username = "admin",
            password = passwordEncoder.encode("admin"),
            rolesANDprivileges = listOf("ROLE_ADMIN") )

        // MY LOGGER IS BETTER THAN YOURS AND YOU SHOULD KNOW IT
        //System.out.println(data[username].toString())

        return data[username]
    }
}