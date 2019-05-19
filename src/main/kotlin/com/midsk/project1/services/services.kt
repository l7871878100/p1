package com.midsk.project1.services

import com.midsk.project1.dao.MessageRepository
import com.midsk.project1.dao.UserAuthorityRepository
import com.midsk.project1.dao.UserRepository
import com.midsk.project1.models.Message
import com.midsk.project1.models.User
import com.midsk.project1.models.UserAuthority
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.lang.Error
import java.util.*
import java.util.logging.Logger

interface IMessageService {
    fun saveMessage(message: Message): Message

    fun findById(id: Long): Optional<Message>
    fun findAllByTheme(pageable: Pageable): Page<Message>
    fun findAllByReplyList(id: Long,pageable: Pageable): Page<Message>
    fun findAll(pageable: Pageable): Page<Message>

}


interface IUserService : UserDetailsService {

    fun saveUser(user: User): User

    fun findById(id: Long): Optional<User>
    fun findByUsername(username: String?): Optional<User>
}

interface IUserAuthorityService {
    fun findById(role: String): Optional<UserAuthority>

    fun saveAuthority(authority: UserAuthority): UserAuthority
}


@Service
class MessageService(val repository: MessageRepository) : IMessageService {
    override fun findAll(pageable: Pageable): Page<Message> {
        return repository.findAll(pageable)
    }

    override fun findAllByReplyList(id: Long,pageable: Pageable): Page<Message> {
        return repository.findAllByIdOrReplyId(id,id,pageable)
    }

    override fun findAllByTheme(pageable: Pageable): Page<Message> {
        return repository.findAllByThemeIsNotNullAndDisplayIsTrue(pageable = pageable)
    }

    @Transactional
    override fun saveMessage(message: Message): Message {
        return repository.save(message)
    }

    override fun findById(id: Long): Optional<Message> {
        return repository.findById(id)
    }
}

@Service
class UserService(private val repository: UserRepository) : IUserService {

    companion object {

        val logger = Logger.getLogger(UserService::class.java.name)!!
    }

    override fun loadUserByUsername(username: String?): UserDetails {

        var user: UserDetails? = null
        if(username ==null){
            throw Error("没有找到该用户：$username")
        }
        repository.findByUsername(username).ifPresent {
            user = it
        }
        if (user != null) {
            logger.info("用户 $username 登录, ${Date().toLocaleString()}")
            return user!!
        } else {
            throw Error("没有找到该用户：$username")
        }
    }

    @Transactional
    override fun saveUser(user: User): User {
        return repository.save(user)
    }

    override fun findById(id: Long): Optional<User> {

        return repository.findById(id)
    }

    override fun findByUsername(username: String?): Optional<User> {
        if(username==null){
            return Optional.empty()
        }
        return repository.findByUsername(username)
    }
}

@Service
class UserAuthorityService(private val repository: UserAuthorityRepository) : IUserAuthorityService {
    @Transactional
    override fun saveAuthority(authority: UserAuthority): UserAuthority {
        return repository.save(authority)
    }

    override fun findById(role: String): Optional<UserAuthority> {
        return repository.findByRole(role)
    }

}