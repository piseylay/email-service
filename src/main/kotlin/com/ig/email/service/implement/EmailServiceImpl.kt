package com.ig.email.service.implement

import com.ig.email.controller.UserController
import com.ig.email.model.custom.ResponseObject
import com.ig.email.service.EmailService
import freemarker.template.Configuration
import freemarker.template.Template
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import javax.mail.internet.InternetAddress
import javax.mail.util.ByteArrayDataSource

@Service
class EmailServiceImpl : EmailService {

    @Autowired
    lateinit var mailSender: JavaMailSender

    @Autowired
    lateinit var configuration: Configuration

    @Autowired
    lateinit var userController: UserController

    @Autowired
    lateinit var response: ResponseObject

    @Value( "\${mail_sender_name}")
    private val mailSenderName: String? = null

    @Value("\${spring.mail.username}")
    private val emailAddress: String? = null

    @Throws(Exception::class)
    override fun sendEmailToUser(toEmail: Array<InternetAddress>, subject: String, message: String): MutableMap<String, Any> {
        val msg = mailSender.createMimeMessage()
        val helper = MimeMessageHelper(msg, true)
        helper.setFrom(InternetAddress("$mailSenderName ${"<"} $emailAddress ${">"}"))
        helper.setBcc(toEmail)
        helper.setSubject(subject)
        helper.setText(message, true)
        mailSender.send(msg)
        return response.responseStatusCode(200, "Mail was sent successfully to $toEmail")
    }

    override fun sendEmailWithAttachment(toEmail: Array<InternetAddress>, subject: String, message: String, file: MultipartFile): MutableMap<String, Any> {

        synchronized(this) {
            val listUser = userController.getAllUser()
            val html: Template = configuration.getTemplate("test.html")

//            val html = listUser!!.view
            val msg = mailSender.createMimeMessage()
            val helper = MimeMessageHelper(msg, true)
            helper.setFrom(InternetAddress("$mailSenderName ${"<"} $emailAddress ${">"}"))
            helper.setBcc(toEmail)
            helper.setSubject(subject)
            helper.setText(message+"$html", true)
            val attachment = ByteArrayDataSource(file.inputStream, "application/octet-stream")
            helper.addAttachment(file.originalFilename!!, attachment)
            mailSender.send(msg)
            return response.responseStatusCode(200, "Success!!!!!!!!!")
        }
    }
}