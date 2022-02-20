package com.example.webFluxJWT.model

import com.fasterxml.jackson.annotation.JsonProperty


data class AuthResponse(
    val tokens: Map<String,String>,
)