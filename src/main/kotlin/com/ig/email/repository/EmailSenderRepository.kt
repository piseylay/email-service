package com.ig.email.repository

import com.ig.email.model.MailSender
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface EmailSenderRepository : JpaRepository<MailSender, Long> {
}