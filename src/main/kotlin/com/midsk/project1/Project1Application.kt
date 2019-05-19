package com.midsk.project1

import com.midsk.project1.models.User
import com.midsk.project1.models.UserAuthority
import com.midsk.project1.services.IUserAuthorityService
import com.midsk.project1.services.IUserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.security.crypto.password.PasswordEncoder
import javax.annotation.PostConstruct

@SpringBootApplication
class Project1Application {
    @Autowired
    private lateinit var userAuthorityService: IUserAuthorityService
    @Autowired
    private lateinit var userService: IUserService
    @Autowired
    private lateinit var passwordEncoder: PasswordEncoder


    @PostConstruct
    fun init() {
        val authority = userAuthorityService.findById("ADMIN")
        val auth: UserAuthority = if (!authority.isPresent) {
            val authorityTemp = UserAuthority()
            authorityTemp.role = "ADMIN"
            userAuthorityService.saveAuthority(authorityTemp)
        } else {
            authority.get()
        }

        val user = userService.findByUsername("admin")
        if (!user.isPresent) {
            val userAccount = User("admin", passwordEncoder.encode("admin12345678"))
            userAccount.addAuthority(auth)
            userService.saveUser(userAccount)
        }

    }
}

fun main(args: Array<String>) {
    runApplication<Project1Application>(*args)
}

