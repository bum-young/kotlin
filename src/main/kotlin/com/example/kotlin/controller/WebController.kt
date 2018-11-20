package com.example.kotlin.controller

import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping

@Controller
class WebController {

    @RequestMapping(value="/")
    fun home(): String{
        return "home"
    }

    @RequestMapping(value="/user")
    fun welcome(): String{
        return "user"
    }

    @RequestMapping(value="/admin")
    @PreAuthorize("hasRole('ADMIN')")
    fun admin(): String{
        return "admin"
    }

    @RequestMapping(value="/login")
    fun login(): String{
        return "login"
    }

    @RequestMapping(value="/403")
    fun error403(): String{
        return "403"
    }

}