package com.tws.moments.data.repository

import com.tws.moments.data.remote.api.MomentService
import com.tws.moments.data.remote.api.RetrofitInstance
import com.tws.moments.data.remote.api.dto.TweetBean
import com.tws.moments.data.remote.api.dto.UserBean
import com.tws.moments.domain.repository.MomentRepository

class MomentRepositoryImpl(
    private val reqApi: MomentService = RetrofitInstance.momentService
): MomentRepository {
    override suspend fun fetchUser(): UserBean {
        return reqApi.user("jsmith")
    }

    override suspend fun fetchTweets(): List<TweetBean> {
        return reqApi.tweets("jsmith")
    }
}
