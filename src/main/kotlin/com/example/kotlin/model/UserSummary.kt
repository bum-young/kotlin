package com.example.kotlin.model

import lombok.Getter
import lombok.Setter

class UserSummary {
    var id:Long? = null
    var username:String? = null
    var name:String? = null

    constructor(id:Long?, username:String?, name:String?) {
        this.id = id
        this.username = username
        this.name = name
    }
}