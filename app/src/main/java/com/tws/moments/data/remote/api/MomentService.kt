package com.tws.moments.data.remote.api

import com.tws.moments.data.remote.api.dto.TweetBean
import com.tws.moments.data.remote.api.dto.UserBean
import retrofit2.http.GET
import retrofit2.http.Path

interface MomentService {

    /**
     * https://thoughtworks-ios.herokuapp.com/user/jsmith
     */
    @GET("user/{name}")
    suspend fun user(@Path("name") user: String): UserBean

    /**
     * https://thoughtworks-ios.herokuapp.com/user/jsmith/tweets
     */
    @GET("user/{name}/tweets")
    suspend fun tweets(@Path("name") user: String): List<TweetBean>
}