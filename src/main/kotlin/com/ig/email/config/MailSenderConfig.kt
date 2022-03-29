package com.ig.email.config

import com.ig.email.repository.EmailSenderRepository
import org.springframework.beans.BeansException
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.context.EnvironmentAware
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.JavaMailSenderImpl
import java.io.IOException

@Configuration
class MailSenderConfig constructor(
    emailSenderRepository: EmailSenderRepository
) : ApplicationContextAware, EnvironmentAware {

    val emailConfig = emailSenderRepository.findById(1).orElse(null)
        ?: throw Exception("User not present")

    private var applicationContext: ApplicationContext? = null
    private var environment: Environment? = null

    @Throws(BeansException::class)
    override fun setApplicationContext(applicationContext: ApplicationContext) {
        this.applicationContext = applicationContext
    }

    override fun setEnvironment(environment: Environment) {
        this.environment = environment
    }

    @Bean
    @Throws(IOException::class)
    fun mailSender(): JavaMailSender? {
        val mailSender = JavaMailSenderImpl()

        mailSender.host = this.environment!!.getProperty("mail.server.host","${emailConfig.host}")
        mailSender.port = this.environment!!.getProperty("mail.server.port","${emailConfig.port}").toInt()
        mailSender.protocol = this.environment!!.getProperty("mail.server.protocol","${emailConfig.protocol}")
        mailSender.username = this.environment!!.getProperty("mail.server.username","${emailConfig.username}")
        mailSender.password = this.environment!!.getProperty("mail.server.password","${emailConfig.password}")

        return mailSender
    }
}