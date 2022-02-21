package com.example.webFluxJWT.model

import com.fasterxml.jackson.annotation.JsonProperty

data class RefreshRequest(
    @JsonProperty("token")
    val token: String,
)