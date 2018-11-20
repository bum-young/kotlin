package com.example.kotlin.security.model

import javax.validation.constraints.NotBlank

data class LoginRequest(
        @NotBlank
        val usernameOrEmail:String? = null,
        @NotBlank
        val password:String? = null
) {
    /*@NotBlank
    val usernameOrEmail:String? = null

    @NotBlank
    val password:String? = null*/


}