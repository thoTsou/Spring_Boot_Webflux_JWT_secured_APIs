package com.example.webFluxJWT.model

import com.fasterxml.jackson.annotation.JsonProperty



data class Message(
    @JsonProperty("content")
    val content: String
)
