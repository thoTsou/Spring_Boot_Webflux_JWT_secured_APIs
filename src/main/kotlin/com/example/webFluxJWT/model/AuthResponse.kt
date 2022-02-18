package com.example.webFluxJWT.model

import com.fasterxml.jackson.annotation.JsonProperty


data class AuthResponse(
    @JsonProperty("token")
    val token: String,
)