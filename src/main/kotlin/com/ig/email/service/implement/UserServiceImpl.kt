package com.ig.email.service.implement

import com.ig.email.repository.UserRepository
import com.ig.email.service.UserService
import com.ig.email.util.PdfGenaratorUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.Resource
import org.springframework.core.io.UrlResource
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import java.nio.file.Path
import java.nio.file.Paths

@Service
class UserServiceImpl : UserService {

    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var pdfGenaratorUtil: PdfGenaratorUtil
    override fun getUserInfoPdf(id: Long): ResponseEntity<*>? {
        val user = userRepository.findById(id).orElse(null)
            ?: throw Exception("Student not present")
        val userMap: MutableMap<String, Any> = HashMap<String, Any>()
        userMap["ID"] = user.id.toString()
        userMap["userName"] = user.userName.toString()
        userMap["email"] = user.email.toString()
        userMap["password"] = user.password.toString()
        var resource: Resource? = null
        try {
            val property = "java.io.tmpdir"
            val tempDir = System.getProperty(property)
            val fileNameUrl: String? = pdfGenaratorUtil!!.createPdf("user", userMap)
            val path: Path = Paths.get("$tempDir/$fileNameUrl")
            resource = UrlResource(path.toUri())
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType(MediaType.APPLICATION_PDF_VALUE))
            .header(
                HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + resource!!.filename.toString() + "\""
            )
            .body<Any>(resource)
    }
}