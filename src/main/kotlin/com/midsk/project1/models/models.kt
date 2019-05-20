package com.midsk.project1.models

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.io.Serializable
import java.util.*
import javax.persistence.*
import javax.validation.constraints.Min


@Entity
@EntityListeners(AuditingEntityListener::class)
data class Message(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long = 0,
        @ManyToOne
        var messageQuote: Message? = null,
        var gender: Boolean = false,
        var avatar: Int = 1,
        @Column(length = 100)
        val theme: String? = null,
//        @ManyToOne
        var username: String,
        @Column(columnDefinition = "longtext not null")
        @Min(5)
        var content: String,
        var display: Boolean = false,
        @Column(length = 20)
        var ipAddress: String = "",
        var replyId: Long? = null,
        var deleted: Boolean = false,
        var readNum: Long = 0,
        var replyNum: Long = 0
) : Serializable {

    @LastModifiedDate
    @Temporal(TemporalType.DATE)
    var latestDate: Date? = null

    @CreatedDate
    @Temporal(TemporalType.DATE)
    var createDate: Date? = null

    var latestName: String? = ""

}

