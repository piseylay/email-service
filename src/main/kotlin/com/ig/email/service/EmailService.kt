package com.ig.email.service

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.multipart.MultipartFile
import javax.mail.internet.InternetAddress

interface EmailService {
    fun sendEmailToUser(toEmail: Array<InternetAddress>, subject: String, message: String): MutableMap<String, Any>
    fun sendEmailWithAttachment(toEmail: Array<InternetAddress>, subject: String, message: String, file: MultipartFile): MutableMap<String, Any>?

}