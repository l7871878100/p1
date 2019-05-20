package com.midsk.project1

//import com.midsk.project1.models.User
//import com.midsk.project1.models.UserAuthority
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

//import org.springframework.security.crypto.password.PasswordEncoder
//import javax.annotation.PostConstruct

@SpringBootApplication
@EnableJpaAuditing
class Project1Application {
}

fun main(args: Array<String>) {
    runApplication<Project1Application>(*args)
}

