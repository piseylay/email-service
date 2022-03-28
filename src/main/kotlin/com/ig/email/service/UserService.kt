package com.ig.email.service

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable

interface UserService {
    fun getUserInfoPdf(@PathVariable id: Long): ResponseEntity<*>?
}