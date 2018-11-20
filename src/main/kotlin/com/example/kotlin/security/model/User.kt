package com.example.kotlin.security.model

import com.fasterxml.jackson.annotation.JsonManagedReference
import org.hibernate.annotations.NaturalId

import javax.persistence.*
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

@Entity
@Table(
    name="users", uniqueConstraints = arrayOf(
        UniqueConstraint(columnNames = arrayOf("username")),
        UniqueConstraint(columnNames = arrayOf("email"))
    )
)
class User: DateAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @NotBlank
    @Size(max = 40)
    var name: String? = null

    @NotBlank
    @Size(max = 15)
    var username: String? = null

    @NaturalId
    @NotBlank
    @Size(max = 40)
    @Email
    var email: String? = null

    @NotBlank
    @Size(max = 100)
    var password: String? = null

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name="user_roles", joinColumns = arrayOf(JoinColumn(name="user_id")),
            inverseJoinColumns = arrayOf(JoinColumn(name = "role_id")))
    var roles: Set<Role> = HashSet()

    constructor(){

    }

    constructor(name: String?, username: String?, email: String?, password: String?) {
        this.name = name
        this.username = username
        this.email = email
        this.password = password
    }


}