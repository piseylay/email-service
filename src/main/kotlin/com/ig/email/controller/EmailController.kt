package com.ig.email.controller

import com.ig.email.model.custom.ResponseObject
import com.ig.email.service.EmailService
import org.springframework.core.task.TaskExecutor
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import javax.mail.internet.InternetAddress

@RestController
@RequestMapping("v1/api/email")
class EmailController constructor(
    private val response: ResponseObject,
    private val emailService: EmailService,
    private val taskExecutor: TaskExecutor
) {
    @GetMapping
    fun sendEmailtoEmail (@RequestParam toEmail: Array<InternetAddress>, subject: String, message: String): MutableMap<String, Any> {
        taskExecutor.execute {
            emailService.sendEmailToUser(toEmail, subject, message)
        }
        return response.responseStatusCode(200, "Success")
    }
    @GetMapping("/file")
    fun sendEmailWithAttachment(@RequestParam toEmail: Array<InternetAddress>, subject: String, message: String, file: MultipartFile): MutableMap<String, Any> {
        taskExecutor.execute {
            emailService.sendEmailWithAttachment(toEmail, subject, message, file)
        }
        return response.responseStatusCode(200, "Success!!!")
    }
}