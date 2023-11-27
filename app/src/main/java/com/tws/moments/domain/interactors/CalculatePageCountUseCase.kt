package com.tws.moments.domain.interactors

import com.tws.moments.presentation.viewModels.MainViewModel.Companion.PAGE_TWEET_COUNT
import com.tws.moments.data.remote.api.dto.TweetBean

class CalculatePageCountUseCase {

    operator fun invoke(allTweets: List<TweetBean>?): Int {
        return if (allTweets.isNullOrEmpty()) {
            0
        } else {
            val pageCount = if (allTweets.size % PAGE_TWEET_COUNT == 0) {
                allTweets.size / PAGE_TWEET_COUNT
            } else {
                allTweets.size / PAGE_TWEET_COUNT + 1
            }
            pageCount
        }
    }
}