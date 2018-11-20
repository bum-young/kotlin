package com.example.kotlin.controller

import com.example.kotlin.model.JwtAuthenticationResponse
import com.example.kotlin.security.jwt.JwtTokenProvider
import com.example.kotlin.security.model.LoginRequest
import com.example.kotlin.security.repository.RoleRepository
import com.example.kotlin.security.repository.UserRepository
import org.apache.coyote.Response
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@RequestMapping("/api/auth")
class AuthController {

    @Autowired
    var authenticationManager: AuthenticationManager? = null

    @Autowired
    var userRespository: UserRepository? = null

    @Autowired
    var roleRepository: RoleRepository? = null

    @Autowired
    var passwordEncoder: PasswordEncoder? = null

    @Autowired
    var tokenProvider : JwtTokenProvider? = null

    @PostMapping("/signin")
    fun authenticateUser(@Valid @RequestBody loginRequest:LoginRequest) : ResponseEntity<*> {
        var authentication: Authentication = authenticationManager!!.authenticate(
                UsernamePasswordAuthenticationToken(
                        loginRequest.usernameOrEmail,
                        loginRequest.password
                )
        )

        SecurityContextHolder.getContext().authentication = authentication

        var jwt:String = tokenProvider!!.generateToken(authentication)

        return ResponseEntity.ok(JwtAuthenticationResponse(jwt))
    }

}