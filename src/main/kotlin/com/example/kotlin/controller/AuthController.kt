package com.example.kotlin.controller

import com.example.kotlin.model.ApiResponse
import com.example.kotlin.model.JwtAuthenticationResponse
import com.example.kotlin.model.SignUpRequest
import com.example.kotlin.security.jwt.JwtTokenProvider
import com.example.kotlin.security.model.LoginRequest
import com.example.kotlin.security.model.Role
import com.example.kotlin.security.model.RoleName
import com.example.kotlin.security.model.User
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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.net.URI
import java.util.*
import javax.validation.Valid

@RestController
@RequestMapping("/api/auth")
class AuthController(
        private val authenticationManager:AuthenticationManager,
        private val userRespository:UserRepository,
        private val roleRepository: RoleRepository,
        private val passwordEncoder: PasswordEncoder,
        private val tokenProvider: JwtTokenProvider
) {

    @PostMapping("/signin")
    fun authenticateUser(@Valid @RequestBody loginRequest:LoginRequest) : ResponseEntity<*> {
        var authentication: Authentication = authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(
                        loginRequest.usernameOrEmail,
                        loginRequest.password
                )
        )

        SecurityContextHolder.getContext().authentication = authentication

        var jwt:String = tokenProvider.generateToken(authentication)

        return ResponseEntity.ok(JwtAuthenticationResponse(jwt))
    }

    @PostMapping("/signup")
    fun registerUser(@Valid @RequestBody signUpRequest:SignUpRequest) : ResponseEntity<*>{
        if(userRespository.existsByUsername(signUpRequest.username)){
            return ResponseEntity(ApiResponse(false, "Username is already taken!")
                    , HttpStatus.BAD_REQUEST)
        }
        if(userRespository.existsByEmail(signUpRequest.email)) {
            return ResponseEntity(ApiResponse(false,  "Email Address already in use!")
                    , HttpStatus.BAD_REQUEST)
        }

        var user = User(signUpRequest.name, signUpRequest.username, signUpRequest.email,
                            passwordEncoder.encode(signUpRequest.password))

        var userRole:Role? = roleRepository.findByName(RoleName.ROLE_USER)

        user.roles = Collections.singleton(userRole) as Set<Role>

        var result:User = userRespository.save(user)

        var location:URI = ServletUriComponentsBuilder.fromCurrentContextPath().path("/users/{username}")
                .buildAndExpand(result.username).toUri()

        return ResponseEntity.created(location).body(ApiResponse(true, "User registered successfully"))
    }

}