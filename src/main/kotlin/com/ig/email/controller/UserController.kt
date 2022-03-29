package com.ig.email.controller

import com.ig.email.model.custom.ResponseObject
import com.ig.email.repository.UserRepository
import com.ig.email.service.UserService
import com.ig.email.util.PdfGenaratorUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.Resource
import org.springframework.core.io.UrlResource
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.nio.file.Path
import java.nio.file.Paths


@RestController
@RequestMapping("v1/api/user")
class UserController {

    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var response: ResponseObject

    @Autowired
    lateinit var userService: UserService

//    @GetMapping("/list")
//    fun getAllUser(): ModelAndView? {
//        val template = ModelAndView("test")
//        template.addObject("users", userRepository.findAll())
//        return template
//    }
@Autowired
private var pdfGenaratorUtil: PdfGenaratorUtil? = null

    @GetMapping("/{id}")
    fun getUserInfoPdf(@PathVariable id: Long): ResponseEntity<*>?{
        val user = userRepository.findById(id).orElse(null)
            ?: throw Exception("Student not present")
        val userMap: MutableMap<String, Any> = HashMap()
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

    @GetMapping()
    fun userConvertPdf(@PathVariable id: Long): MutableMap<String, Any> {
        userService.getUserInfoPdf(id)
        return response.responseStatusCode(200,"Success!!")
    }
}