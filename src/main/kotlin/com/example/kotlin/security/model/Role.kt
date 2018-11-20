package com.example.kotlin.security.model

import org.hibernate.annotations.NaturalId
import javax.persistence.*

@Entity
@Table(name="roles")
class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id:Long? = null

    @Enumerated(EnumType.STRING)
    @NaturalId
    @Column(length = 60)
    var name:RoleName? = null

    constructor(){

    }

    constructor(id:Long, name:RoleName) {
        this.id = id
        this.name = name
    }
}