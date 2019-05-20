package com.midsk.project1.dao

import com.midsk.project1.models.Message
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.query.Param

interface MessageRepository:JpaRepository<Message,Long>{
    fun findAllByThemeIsNotNullAndDisplayIsTrueAndDeletedIsFalse(pageable: Pageable): Page<Message>
//    fun findAllByIdOrReplyId(id:Long,pageable: Pageable):Page<Message>
    fun findAllByIdOrReplyIdAndDeletedIsFalseAndDisplayIsTrue(id:Long, replyId:Long):List<Message>
}

//interface UserRepository:JpaRepository<User,Long>{
//    fun findByUsername(username:String):Optional<User>
//}
//
//interface UserAuthorityRepository:JpaRepository<UserAuthority,Long>{
//    fun findByRole(role:String):Optional<UserAuthority>
//}
