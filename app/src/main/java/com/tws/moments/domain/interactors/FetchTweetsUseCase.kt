package com.tws.moments.domain.interactors

import com.tws.moments.data.remote.api.dto.TweetBean
import com.tws.moments.domain.repository.MomentRepository

class FetchTweetsUseCase(private val repository: MomentRepository) {

    suspend operator fun invoke(): List<TweetBean>? {
        return try {
            repository.fetchTweets()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}