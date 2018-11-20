package com.example.kotlin.model

import lombok.Getter
import lombok.Setter

class JwtAuthenticationResponse {
    var accessToken:String? = null
    var tokenType:String = "Bearer"

    constructor(accessToken:String?) {
        this.accessToken = accessToken
    }
}