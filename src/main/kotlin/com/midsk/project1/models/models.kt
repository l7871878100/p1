package com.midsk.project1.models

import java.io.Serializable
import javax.persistence.*
import javax.validation.constraints.Min


@Entity
data class Message(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long=0,
        @ManyToOne
        var messageQuote:Message?=null,
        @Column(length = 100)
        val theme:String?=null,
        @ManyToOne
        var user: User?=null,
        @Column(columnDefinition = "text not null")
        @Min(5)
        var content:String,
        var display:Boolean = false,
        @Column(length = 20)
        var ipAddress:String="",
        var replyId:Long?=null,
        var deleted:Boolean=false
) : Serializable

