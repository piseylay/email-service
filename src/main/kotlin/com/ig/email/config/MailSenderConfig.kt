package com.ig.email.config

import org.springframework.beans.BeansException
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.context.EnvironmentAware
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource
import org.springframework.core.env.Environment
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.JavaMailSenderImpl
import java.io.IOException
import java.util.*


@Configuration
@PropertySource("classpath:mail/emailconfig.properties")
class MailSenderConfig : ApplicationContextAware, EnvironmentAware {

    private val JAVA_MAIL_FILE = "classpath:mail/javamail.properties"

    private val HOST = "mail.server.host"
    private val PORT = "mail.server.port"
    private val PROTOCOL = "mail.server.protocol"
    private val USERNAME = "mail.server.username"
    private val PASSWORD = "mail.server.password"

    private var applicationContext: ApplicationContext? = null
    private var environment: Environment? = null

    @Throws(BeansException::class)
    override fun setApplicationContext(applicationContext: ApplicationContext) {
        this.applicationContext = applicationContext
    }


    override fun setEnvironment(environment: Environment) {
        this.environment = environment
    }

    /*
     * SPRING + JAVAMAIL: JavaMailSender instance, configured via .properties files.
     */
    @Bean
    @Throws(IOException::class)
    fun mailSender(): JavaMailSender? {
        val mailSender = JavaMailSenderImpl()

        // Basic mail sender configuration, based on emailconfig.properties
        mailSender.host = this.environment!!.getProperty(HOST)
        mailSender.port = this.environment!!.getProperty(PORT)!!.toInt()
        mailSender.protocol = this.environment!!.getProperty(PROTOCOL)
        mailSender.username = this.environment!!.getProperty(USERNAME)
        mailSender.password = this.environment!!.getProperty(PASSWORD)

        // JavaMail-specific mail sender configuration, based on javamail.properties
        val javaMailProperties = Properties()
        javaMailProperties.load(this.applicationContext!!.getResource(JAVA_MAIL_FILE).inputStream)
        mailSender.javaMailProperties = javaMailProperties
        return mailSender
    }
}