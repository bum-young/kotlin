package com.example.kotlin.model

import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

class SignUpRequest {
    @NotBlank
    @Size(min = 4, max = 40)
    val name:String? = null

    @NotBlank
    @Size(min = 3, max= 15)
    val username:String? = null

    @NotBlank
    @Size(max = 40)
    @Email
    val email:String? = null

    @NotBlank
    @Size(min = 6, max = 20)
    val password:String? = null

}