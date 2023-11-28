package com.tws.moments.domain.interactors

import com.tws.moments.data.remote.api.dto.TweetBean
import com.tws.moments.domain.repository.MomentRepository

class FetchTweetsUseCase(private val repository: MomentRepository) {

    suspend operator fun invoke(): List<TweetBean>? {
        return try {
            val tweets = repository.fetchTweets()
            filterEmptyTweets(tweets)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun filterEmptyTweets(tweets: List<TweetBean>?): MutableList<TweetBean>? {
        return tweets?.filter {
            it.content?.isNotEmpty() == true || it.images?.isNotEmpty() == true
        }?.toMutableList()
    }
}