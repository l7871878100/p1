package com.midsk.project1.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport

@Configuration
@EnableWebSecurity
class MyccConfig:WebSecurityConfigurerAdapter() {
    @Autowired()
    private lateinit var userService: UserDetailsService


    override fun configure(http: HttpSecurity?) {
        http!!.cors().disable().csrf().disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.POST,"/message/publish").permitAll()
                .antMatchers("/**").permitAll()
                .antMatchers("/**.html").permitAll()
                .anyRequest().authenticated()
                .and()
                .userDetailsService(userDetailsService())
    }



    @Bean
    fun passwordEncoder():PasswordEncoder=PasswordEncoderFactories.createDelegatingPasswordEncoder()
}
