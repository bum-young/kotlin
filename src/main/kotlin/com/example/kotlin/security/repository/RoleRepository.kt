package com.example.kotlin.security.repository

import com.example.kotlin.security.model.Role
import com.example.kotlin.security.model.RoleName
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface RoleRepository : JpaRepository<Role, Long> {
    fun findByName(roleName:RoleName) : Role?
}