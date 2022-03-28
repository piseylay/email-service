package com.ig.email.controller

import com.ig.email.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.ModelAndView

@RestController
@RequestMapping("v1/api/user")
class UserController {

    @Autowired
    lateinit var userRepository: UserRepository
    @GetMapping("/list")
    fun getAllUser(): ModelAndView? {
        val template = ModelAndView("test")
        template.addObject("users", userRepository.findAll())
        return template
    }
}