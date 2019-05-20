package com.midsk.project1.services

import com.midsk.project1.dao.MessageRepository
import com.midsk.project1.models.Message
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

interface IMessageService {
    fun saveMessage(message: Message): Message

    fun findById(id: Long): Optional<Message>
    fun findAllByTheme(pageable: Pageable): Page<Message>
    fun findAllByReplyList(id: Long): List<Message>
    fun findAll(pageable: Pageable): Page<Message>

}

@Service
class MessageService(val repository: MessageRepository) : IMessageService {
    override fun findAll(pageable: Pageable): Page<Message> {
        return repository.findAll(pageable)
    }

    override fun findAllByReplyList(id: Long): List<Message> {

        val data = repository.findAllByIdOrReplyIdAndDeletedIsFalseAndDisplayIsTrue(id,id).toMutableList()
        return data

    }

    override fun findAllByTheme(pageable: Pageable): Page<Message> {
        val data= repository.findAllByThemeIsNotNullAndDisplayIsTrueAndDeletedIsFalse(pageable = pageable)
        return data
    }

    @Transactional
    override fun saveMessage(message: Message): Message {
        return repository.save(message)
    }

    override fun findById(id: Long): Optional<Message> {
        return repository.findById(id)
    }
}
