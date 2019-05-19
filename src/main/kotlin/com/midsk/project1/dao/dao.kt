package com.midsk.project1.dao

import com.midsk.project1.models.Message
import com.midsk.project1.models.User
import com.midsk.project1.models.UserAuthority
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.query.Param
import java.util.*

interface MessageRepository:JpaRepository<Message,Long>{
    fun findAllByThemeIsNotNullAndDisplayIsTrue(pageable: Pageable): Page<Message>
//    fun findAllByIdOrReplyId(id:Long,pageable: Pageable):Page<Message>
    fun findAllByIdOrReplyId(@Param("id")id:Long, @Param("replyId") replyId:Long, pageable: Pageable):Page<Message>
}

interface UserRepository:JpaRepository<User,Long>{
    fun findByUsername(username:String):Optional<User>
}

interface UserAuthorityRepository:JpaRepository<UserAuthority,Long>{
    fun findByRole(role:String):Optional<UserAuthority>
}
