package com.example.webFluxJWT.controller

import com.example.webFluxJWT.model.Message
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/my-api")
class TestController {

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    suspend fun giveMessageToAdmin(): ResponseEntity<Message> =
        ResponseEntity.ok(Message("Content for admins only"))

    @GetMapping("/user")
    @PreAuthorize("hasRole('USER')")
    suspend fun giveMessageToUser(): ResponseEntity<Message> =
        ResponseEntity.ok(Message("Content for users only"))

}