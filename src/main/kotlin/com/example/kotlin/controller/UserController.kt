package com.example.kotlin.controller

import com.example.kotlin.model.CurrentUser
import com.example.kotlin.model.UserSummary
import com.example.kotlin.security.model.User
import com.example.kotlin.security.model.UserIdentityAvailability
import com.example.kotlin.security.repository.UserRepository
import com.example.react.security.model.UserPrincipal
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api")
class UserController {

    val log = LoggerFactory.getLogger(this.javaClass)

    @Autowired
    private var userRepository:UserRepository? = null

    @GetMapping("/user/me")
    @PreAuthorize("hasRole('USER')")
    fun getCurrentUser(@CurrentUser currentUser:UserPrincipal) : UserSummary {
        var userSummary:UserSummary = UserSummary(currentUser.id, currentUser.username, currentUser.name)
        return userSummary
    }

    @GetMapping("/users/{name}")
    fun usersName(@PathVariable name:String) : ResponseEntity<*> {
        log.info("=] name : ${name}")

        val principal:UserPrincipal = UserPrincipal(
                "1".toLong(),
                name,
                "usernameValue",
                "test@email.com",
                "pass",
                null
        )

        return ResponseEntity.ok(principal)
    }

    @GetMapping("/user/checkUsernameAvailability")
    fun checkUsernameAvailability(@RequestParam(value="username") username:String) : UserIdentityAvailability {
        return UserIdentityAvailability(!userRepository!!.existsByUsername(username))
    }

    @GetMapping("/user/checkEmailAvailability")
    fun checkEmailAvailability(@RequestParam(value="email") email:String) : UserIdentityAvailability {
        return UserIdentityAvailability(!userRepository!!.existsByEmail(email))
    }



}