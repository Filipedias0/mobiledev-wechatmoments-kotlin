package com.tws.moments.domain.interactors

import com.tws.moments.presentation.viewModels.MainViewModel.Companion.PAGE_TWEET_COUNT
import kotlin.math.min
import com.tws.moments.data.remote.api.dto.TweetBean

class PaginateTweetsUseCase {

    operator fun invoke(allTweets: List<TweetBean>?, pageIndex: Int): List<TweetBean>? {
        if (allTweets.isNullOrEmpty() || pageIndex < 0 || pageIndex > calculatePageCount(allTweets)) {
            return null
        }

        val startIndex = PAGE_TWEET_COUNT * pageIndex
        val endIndex = min(allTweets.size, PAGE_TWEET_COUNT * (pageIndex + 1))
        return allTweets.subList(startIndex, endIndex)
    }

    private fun calculatePageCount(allTweets: List<TweetBean>): Int {
        return if (allTweets.size % PAGE_TWEET_COUNT == 0) {
            allTweets.size / PAGE_TWEET_COUNT
        } else {
            allTweets.size / PAGE_TWEET_COUNT + 1
        }
    }
}