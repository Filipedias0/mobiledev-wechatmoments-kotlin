package com.tws.moments.testUtils

import com.tws.moments.data.remote.api.dto.CommentBean
import com.tws.moments.data.remote.api.dto.ImageBean
import com.tws.moments.data.remote.api.dto.SenderBean
import com.tws.moments.data.remote.api.dto.TweetBean
import com.tws.moments.data.remote.api.dto.UserBean

object TestUtils {
    fun generateFakeTweets(count: Int): List<TweetBean> {
        val tweets = mutableListOf<TweetBean>()

        for (i in 1..count) {
            val sender = SenderBean("username$i", "nick$i", "avatarUrl$i")
            val images = listOf(ImageBean("imageUrl1"), ImageBean("imageUrl2")) // Lista de imagens fictícias
            val comments = listOf(CommentBean("Comment $i", sender)) // Lista de comentários fictícios

            val tweet = TweetBean(
                "Tweet content $i",
                sender,
                images,
                comments,
                null
            )

            tweets.add(tweet)
        }

        return tweets
    }

    fun generateFakeUser(): UserBean {
        return UserBean(
            username = "fake_username",
            profileImage = "fake_profile_image_url",
            avatar = "fake_avatar_url",
            nick = "fake_nick"
        )
    }

    fun generateAlternateFakeUser(): UserBean {
        return UserBean(
            username = "alternate_fake_username",
            profileImage = "alternate_fake_profile_image_url",
            avatar = "alternate_fake_avatar_url",
            nick = "alternate_fake_nick"
        )
    }
}
