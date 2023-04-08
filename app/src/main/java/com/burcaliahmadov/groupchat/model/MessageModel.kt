package com.burcaliahmadov.groupchat.model

data class MessageModel(
    var message: String?="",
    var senderId:String?="",
    var timeStamp:Long?=0
)
