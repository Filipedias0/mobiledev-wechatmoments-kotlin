package com.tws.moments.domain.repository

import com.tws.moments.data.remote.api.dto.TweetBean
import com.tws.moments.data.remote.api.dto.UserBean

interface MomentRepository {
    suspend fun fetchUser(): UserBean

    suspend fun fetchTweets(): List<TweetBean>
}