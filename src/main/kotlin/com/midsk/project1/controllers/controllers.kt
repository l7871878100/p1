package com.midsk.project1.controllers

import com.midsk.project1.models.Message
import com.midsk.project1.services.IMessageService
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import java.io.Serializable
import java.security.Principal
import java.util.logging.Logger
import javax.servlet.http.HttpServletRequest


@Throws(Exception::class)
fun getIpAddr(request: HttpServletRequest): String {

    val headers = request.headerNames
    if (headers.toList().contains("X-FORWARDED-FOR")) {
        val ips = request.getHeader("X-FORWARDED-FOR").split(",")

        if (ips.isNotEmpty()) {
            return ips[0]
        }
    }
    return request.remoteAddr
}


@RestController
class MainController(private val messageService: IMessageService) {


    /**
     * 消息主题列表
     */

    @GetMapping("/messages")
    @CrossOrigin
    fun index(
            @PageableDefault(size = 15, page = 0) pageable: Pageable
    ): List<Message> {
        return messageService.findAllByTheme(pageable).content
    }


    /**
     * 用户发布消息接口
     */
    @PostMapping("/message/publish")
    fun publishMessage(@RequestBody message: Message, request: HttpServletRequest): Message {
//        val message = toMessage(form = messageForm)
        message.ipAddress = getIpAddr(request)


        val result = messageService.saveMessage(message)
        if (message.replyId != null && message.replyId!! > 0) {
            messageService.findById(message.replyId!!).ifPresent {
                it.replyNum++
                it.readNum++
                messageService.saveMessage(it)
            }
        }
        return result
    }


    //    获取消息详情 并且已读＋1
    @GetMapping("/message/{id}")
    fun getMessage(@PathVariable("id") message: Message): Message {
        message.readNum++
        return messageService.saveMessage(message)
    }


    /**
     * 用户回复消息接口 回复+1
     */
    @PostMapping("/message/{id}/reply")
    fun replyMessage(@PathVariable("id") message: Message, @RequestBody messageForm: MessageForm, request: HttpServletRequest): Boolean {
        val reply = toMessage(form = messageForm)
        reply.ipAddress = getIpAddr(request)
        reply.replyId = message.id
        message.replyNum++
        messageService.saveMessage(reply)
        messageService.saveMessage(message)
        return true
    }


    private fun toMessage(form: MessageForm): Message {
        return Message(theme = form.theme, content = form.message, username = form.username, replyId = form.replyId)
    }


    /**
     * 消息主题列表
     */

    @GetMapping("/admin/messages")
    @CrossOrigin
    fun adminIndex(
            @PageableDefault(size = 15, page = 0) pageable: Pageable
    ): List<Message> {
        return messageService.findAll(pageable).content
    }

    /**
     * 消息和回复列表
     */
    @GetMapping("/messages/{id}/replyList")
    fun replyMessageList(@PathVariable("id") id: Long
    ): List<Message> {
        val data = messageService.findAllByReplyList(id)
        return data
    }

    class MessageForm(
            var username: String,
            var message: String,
            var theme: String? = null,
            var replyId: Long? = null
    ) : Serializable
}


/**
 * 管理员接口和页面
 */

@Controller
class AdminController(private val messageService: IMessageService) {
    companion object {

        val logger = Logger.getLogger(AdminController::javaClass.name)!!
    }

    /**
     * 管理员删除回复接口
     */
    @PostMapping("/admin/delete/{id}")
    @ResponseBody
    fun deleteMessage(@PathVariable("id") message: Message): Boolean {
        message.deleted = true
        messageService.saveMessage(message)
        logger.info("管理员删除消息: ${message.id}")
        return true
    }

    /**
     * 管理员消息显示
     */

    @PostMapping("/admin/display/{id}")
    @ResponseBody
    fun displayMessage(@PathVariable("id") message: Message): Boolean {
        message.display = true
        message.deleted = false
        messageService.saveMessage(message)
        logger.info("管理员审核显示消息: ${message.id}")
        return true
    }
}