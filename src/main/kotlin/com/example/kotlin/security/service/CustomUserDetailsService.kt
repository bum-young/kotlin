package com.example.kotlin.security.service

import com.example.kotlin.security.model.User
import com.example.kotlin.security.repository.UserRepository
import com.example.react.security.model.UserPrincipal
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import java.util.*
import javax.transaction.Transactional

@Service
class CustomUserDetailsService(
  private val userRepository: UserRepository
):UserDetailsService {

    @Transactional
    override fun loadUserByUsername(username: String?): UserDetails {
        val user: User? = userRepository.findByUsernameOrEmail(username, username)
                .orElseThrow{UsernameNotFoundException("User not found with username or email : $username")}

        return UserPrincipal.create(user)
    }

    fun loadUserById(id:Long): UserDetails {
        val user:User? = userRepository.findById(id).orElseThrow { UsernameNotFoundException("User not found with username or email : $id") }

        return UserPrincipal.create(user)
    }
}