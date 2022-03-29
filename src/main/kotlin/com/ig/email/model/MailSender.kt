package com.ig.email.model

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
data class MailSender (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0L,
    var host: String? = null,
    var port: Int? = 0,
    var protocol: String? = null,
    var username: String? = null,
    var password: String? = null
    )