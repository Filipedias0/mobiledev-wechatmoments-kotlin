package com.tws.moments.data.repository

import android.util.Log
import com.tws.moments.data.remote.api.MomentService
import com.tws.moments.data.remote.api.RetrofitInstance
import com.tws.moments.data.remote.api.dto.TweetBean
import com.tws.moments.data.remote.api.dto.UserBean
import com.tws.moments.domain.repository.MomentRepository

class MomentRepositoryImpl(
    private val reqApi: MomentService
): MomentRepository {
    override suspend fun fetchUser(): UserBean {
        val teste = reqApi.user("jsmith")
        Log.d("fetchUser", teste.username.toString())
        return teste
    }

    override suspend fun fetchTweets(): List<TweetBean> {
        val teste = reqApi.tweets("jsmith")
        Log.d("fetchTweets", teste[0].content.toString())

        return teste
    }
}
