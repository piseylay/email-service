package com.ig.email.model

import javax.persistence.*

@Entity
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = 0,
    @Column(name = "user_name")
    var userName: String? = null,
    var password: String? = null,
    var email: String? = null,
)
