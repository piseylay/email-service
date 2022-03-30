package com.ig.email.service.implement

import com.ig.email.config.MailSenderConfig
import com.ig.email.model.User
import com.ig.email.model.custom.ResponseObject
import com.ig.email.repository.UserRepository
import com.ig.email.service.EmailService
import freemarker.template.Configuration
import freemarker.template.TemplateException
import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.IOException
import java.io.StringWriter
import java.util.*
import javax.mail.internet.InternetAddress
import javax.mail.util.ByteArrayDataSource
import kotlin.collections.HashMap


@Service
class EmailServiceImpl constructor(
    private var mailSender: JavaMailSender,
    private var mailSenderConfig: MailSenderConfig,
    private var configuration: Configuration,
    private var response: ResponseObject,
    private var userRepository: UserRepository,
) : EmailService {

    @Value( "\${mail_sender_name}")
    private val mailSenderName: String? = null
    private val emailAddress = mailSenderConfig.emailConfig.username

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
//            val html: Template = configuration.getTemplate("user.html")
            val html = getEmailContent()
            val msg = mailSender.createMimeMessage()
            val helper = MimeMessageHelper(msg, true)
            helper.setFrom(InternetAddress("$mailSenderName ${"<"} $emailAddress ${">"}"))
            helper.setBcc(toEmail)
            helper.setSubject(subject)
            helper.setText(message+"$html", true)
            val attachment = ByteArrayDataSource(file.inputStream, "application/octet-stream")
            helper.addAttachment(file.originalFilename!!, attachment)
            mailSender.send(msg)
            return response.responseStatusCode(200, "Mail was sent successfully to $toEmail")
        }
    }

    @Throws(IOException::class, TemplateException::class)
    fun getEmailContent(): String? {
        val stringWriter = StringWriter()
        val model: MutableMap<String, Any> = HashMap()
        model["users"] = userRepository.findById(1).get()
        configuration.getTemplate("test.html").process(model, stringWriter)
        return stringWriter.buffer.toString()
    }

    override fun sendEmailFromDatabase(): MutableMap<String, Any> {
        val user = userRepository.findAll()
        val toEmail = user.forEach {
            val subject = "Hello"
            val message = "Hi"
            val msg = mailSender.createMimeMessage()
            val helper = MimeMessageHelper(msg, true)
            helper.setFrom(InternetAddress("$mailSenderName ${"<"} $emailAddress ${">"}"))
            helper.setBcc(it.email.toString())
            helper.setSubject(subject)
            helper.setText(message, true)
            mailSender.send(msg)
        }
        return response.responseStatusCode(200, "Mail was sent successfully to $toEmail")
    }
}