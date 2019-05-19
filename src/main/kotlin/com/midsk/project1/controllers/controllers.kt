package com.midsk.project1.controllers

import com.midsk.project1.models.Message
import com.midsk.project1.models.User
import com.midsk.project1.services.IMessageService
import com.midsk.project1.services.IUserService
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
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
class MainController(
        private val messageService: IMessageService,
        private val userService: IUserService
) {

    /**
     * 普通用户发布消息接口
     */
    @PostMapping("/message/publish")
    fun publishMessage(@RequestBody messageForm: MessageForm, request: HttpServletRequest): Boolean {
        val message = toMessage(form = messageForm)
        message.ipAddress = getIpAddr(request)
        messageService.saveMessage(message)
        return true
    }

    /**
     * 普通用户回复消息接口
     */
    @PostMapping("/message/{id}/reply")
    fun replyMessage(@RequestBody messageForm: MessageForm, request: HttpServletRequest): Boolean {
        val message = toMessage(form = messageForm)
        message.ipAddress = getIpAddr(request)
        messageService.saveMessage(message)
        return true
    }

    @GetMapping("/current-username")
    fun currentUserMessage(principal: Principal?): String? = principal?.name

    private fun toMessage(form: MessageForm): Message {
        var user: User? = null
        var quoteMessage: Message? = null
        if (form.quoteId != null) {
            messageService.findById(form.quoteId!!).ifPresent {
                quoteMessage = it
            }
        }
        userService.findByUsername(form.username).ifPresent {
            user = it
        }
        return Message(theme = form.theme, content = form.message, user = user, messageQuote = quoteMessage)
    }


    @GetMapping("/messages/theme")
    @CrossOrigin
    fun index(
            @RequestParam(value = "page", defaultValue = "0", required = false) page: Int,
            @RequestParam(required = false, value = "size", defaultValue = "15") size: Int,
            principal: Principal?
    ): List<Message> {
        return if(principal==null)
            messageService.findAllByTheme(PageRequest(page, size, Sort(Sort.Direction.DESC, "id"))).content
        else{
            var result:MutableList<Message> = mutableListOf()
            userService.findByUsername(principal.name).ifPresent { user ->
                if(user.authorities.any { it.authority =="ADMIN" }){
                    result.addAll(messageService.findAll(PageRequest(page, size, Sort(Sort.Direction.DESC, "id"))).content)
                }
            }
            result
        }
    }

    @GetMapping("/messages/{id}/replyList")
    fun replyMessageList(@PathVariable("id") id: Long,
                         @RequestParam(value = "page", defaultValue = "0", required = false) page: Int,
                         @RequestParam(required = false, value = "size", defaultValue = "15") size: Int
    ): List<Message> {
        return messageService.findAllByReplyList(id, PageRequest(page, size, Sort(Sort.Direction.DESC, "id"))).content
    }


    class MessageForm(
            var message: String,
            var theme: String? = null,
            var username: String? = null,
            var quoteId: Long? = null
    ) : Serializable
}


/**
 * 管理员接口和页面
 */

@Controller
class AdminController(private val messageService: IMessageService,
                      private val userService: IUserService) {
    companion object {

        val logger = Logger.getLogger(AdminController::javaClass.name)!!
    }

    /**
     * 管理员页面
     */

    @GetMapping("/admin")
    fun admin(): String = "admin"


    /**
     * 管理员审核接口
     */
    @PostMapping("/admin/audit/{id}")
    @ResponseBody
    fun auditMessage(@PathVariable("id") id: Long, @RequestBody auditForm: AuditForm, principal: Principal): Boolean {

        if (auditForm.audited) {
            messageService.findById(id).ifPresent {
                it.display = true
                messageService.saveMessage(it)
            }
        }
        return true
    }

    /**
     * 管理员回复接口
     */
    @PostMapping("/admin/reply/{id}")
    @ResponseBody
    fun replyMessage(@PathVariable("id") id: Long, @RequestBody auditForm: AuditForm, principal: Principal, request: HttpServletRequest): Boolean {

        messageService.findById(id).ifPresent { message ->
            message.display = true
            messageService.saveMessage(message)
            logger.info("管理员审核消息: $id")
            userService.findByUsername(principal.name).ifPresent {

                val replyMessage = Message(replyId = id, user = it, content = auditForm.replyMessage, display = true, ipAddress = getIpAddr(request))
                messageService.saveMessage(replyMessage)
                logger.info("管理员回复消息 $id： ${auditForm.replyMessage},ip 地址： ${replyMessage.ipAddress}")
            }
        }
        return true
    }

    /**
     * 管理员删除回复接口
     */
    @DeleteMapping("/admin/delete/{id}")
    @ResponseBody
    fun deleteMessage(@PathVariable("id") id: Long, principal: Principal): Boolean {
        messageService.findById(id).ifPresent {
            it.display = false
            it.deleted = true
            messageService.saveMessage(it)
            logger.info("管理员删除消息: $id")
        }
        return true
    }

    @GetMapping("/isManage")
    @ResponseBody
    fun isManage(principal: Principal?): Boolean {
        var result = false
        if (principal != null) {
            val user = userService.findByUsername(principal.name)
            if (user.isPresent) {
                val u = user.get()
                val r = u.authorities.any { it.authority == "ADMIN" }
                result = r
            }
        }
        return true
    }


    class AuditForm(var audited: Boolean = true, var replyMessage: String = "") : Serializable
}