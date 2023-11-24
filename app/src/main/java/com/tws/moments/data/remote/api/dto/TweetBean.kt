package com.tws.moments.data.remote.api.dto

data class TweetBean(
    val content: String?,
    val sender: SenderBean?,
    val images: List<ImageBean>?,
    val comments: List<CommentBean>?,
    val error: String?
)