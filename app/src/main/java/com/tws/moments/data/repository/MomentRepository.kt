package com.tws.moments.data.repository

import com.tws.moments.data.remote.api.MomentService
import com.tws.moments.data.remote.api.dto.TweetBean
import com.tws.moments.data.remote.api.dto.UserBean

class MomentRepository(
    private val reqApi: MomentService
) {
    suspend fun fetchUser(): UserBean {
        return reqApi.user("jsmith")
    }

    suspend fun fetchTweets(): List<TweetBean> {
        return reqApi.tweets("jsmith")
    }
}
