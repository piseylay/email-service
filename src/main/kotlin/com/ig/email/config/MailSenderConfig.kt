package com.ig.email.config

import org.springframework.beans.BeansException
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.context.EnvironmentAware
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource
import org.springframework.context.support.ResourceBundleMessageSource
import org.springframework.core.env.Environment
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.JavaMailSenderImpl
import org.thymeleaf.TemplateEngine
import org.thymeleaf.spring5.SpringTemplateEngine
import org.thymeleaf.templatemode.TemplateMode
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver
import org.thymeleaf.templateresolver.ITemplateResolver
import org.thymeleaf.templateresolver.StringTemplateResolver
import java.io.IOException
import java.util.*


@Configuration
@PropertySource("classpath:mail/emailconfig.properties")
class MailSenderConfig : ApplicationContextAware, EnvironmentAware {
    val EMAIL_TEMPLATE_ENCODING = "UTF-8"

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

//    @Bean
//    fun emailMessageSource(): ResourceBundleMessageSource? {
//        val messageSource = ResourceBundleMessageSource()
//        messageSource.setBasename("mail/MailMessages")
//        return messageSource
//    }
//
//    @Bean
//    fun emailTemplateEngine(): TemplateEngine? {
//        val templateEngine = SpringTemplateEngine()
//        // Resolver for TEXT emails
//        templateEngine.addTemplateResolver(textTemplateResolver())
//        // Resolver for HTML emails (except the editable one)
//        templateEngine.addTemplateResolver(htmlTemplateResolver())
//        // Resolver for HTML editable emails (which will be treated as a String)
//        templateEngine.addTemplateResolver(stringTemplateResolver())
//        // Message source, internationalization specific to emails
//        templateEngine.setTemplateEngineMessageSource(emailMessageSource())
//        return templateEngine
//    }
//
//    private fun textTemplateResolver(): ITemplateResolver {
//        val templateResolver = ClassLoaderTemplateResolver()
//        templateResolver.order = Integer.valueOf(1)
//        templateResolver.resolvablePatterns = Collections.singleton("text/*")
//        templateResolver.prefix = "/mail/"
//        templateResolver.suffix = ".txt"
//        templateResolver.templateMode = TemplateMode.TEXT
//        templateResolver.characterEncoding = EMAIL_TEMPLATE_ENCODING
//        templateResolver.isCacheable = false
//        return templateResolver
//    }
//
//    private fun htmlTemplateResolver(): ITemplateResolver? {
//        val templateResolver = ClassLoaderTemplateResolver()
//        templateResolver.order = Integer.valueOf(2)
//        templateResolver.resolvablePatterns = Collections.singleton("html/*")
//        templateResolver.prefix = "/mail/"
//        templateResolver.suffix = ".html"
//        templateResolver.templateMode = TemplateMode.HTML
//        templateResolver.characterEncoding = EMAIL_TEMPLATE_ENCODING
//        templateResolver.isCacheable = false
//        return templateResolver
//    }
//
//    private fun stringTemplateResolver(): ITemplateResolver? {
//        val templateResolver = StringTemplateResolver()
//        templateResolver.order = Integer.valueOf(3)
//        // No resolvable pattern, will simply process as a String template everything not previously matched
//        templateResolver.setTemplateMode("HTML5")
//        templateResolver.isCacheable = false
//        return templateResolver
//    }
}