package com.example.webFluxJWT.model

import com.fasterxml.jackson.annotation.JsonProperty


data class AuthRequest(
    @JsonProperty("username")
    val username: String,
    @JsonProperty("password")
    val password: String
)