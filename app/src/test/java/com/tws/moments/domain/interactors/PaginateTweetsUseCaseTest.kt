package com.tws.moments.domain.interactors

import com.tws.moments.data.remote.api.dto.TweetBean
import com.tws.moments.presentation.viewModels.MainViewModel.Companion.PAGE_TWEET_COUNT
import com.tws.moments.testUtils.TestUtils
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class PaginateTweetsUseCaseTest {

    private lateinit var paginateTweetsUseCase: PaginateTweetsUseCase

    @Before
    fun setUp() {
        paginateTweetsUseCase = PaginateTweetsUseCase()
    }

    @Test
    fun `paginateTweets returns expected sublist when input is valid`() {
        val allTweets = TestUtils.generateFakeTweets(5)
        val pageIndex = 0

        val result = paginateTweetsUseCase(allTweets, pageIndex)

        assertEquals(allTweets.size, result?.size)
    }


    @Test
    fun `paginateTweets returns null when pageIndex is out of range`() {
        val allTweets = TestUtils.generateFakeTweets(3)

        val pageIndex = 2

        val result = paginateTweetsUseCase(allTweets, pageIndex)

        assertNull(result)
    }

    @Test
    fun `paginateTweets returns null when allTweets is null or empty`() {
        val allTweets: List<TweetBean>? = null

        val pageIndex = 0

        val result = paginateTweetsUseCase(allTweets, pageIndex)

        assertNull(result)
    }
}
