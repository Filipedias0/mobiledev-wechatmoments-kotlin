package com.tws.moments.domain.interactors

import com.tws.moments.data.remote.api.dto.TweetBean
import com.tws.moments.testUtils.TestUtils
import org.junit.Assert.assertEquals
import org.junit.Test

class CalculatePageCountUseCaseTest {

    @Test
    fun `calculate page count with empty list should return 0`() {
        val useCase = CalculatePageCountUseCase()
        val emptyList: List<TweetBean> = emptyList()

        val pageCount = useCase(emptyList)

        assertEquals(0, pageCount)
    }

    @Test
    fun `calculate page count with less than page size should return 2`() {
        val useCase = CalculatePageCountUseCase()
        val tweetList: List<TweetBean> = TestUtils.generateFakeTweets(8)

        val pageCount = useCase(tweetList)

        assertEquals(2, pageCount)
    }

    @Test
    fun `calculate page count with exact page size should return correct count`() {
        val useCase = CalculatePageCountUseCase()
        val tweetList: List<TweetBean> = TestUtils.generateFakeTweets(10)

        val pageCount = useCase(tweetList)

        assertEquals(2, pageCount)
    }

    @Test
    fun `calculate page count with more than page size should return correct count`() {
        val useCase = CalculatePageCountUseCase()
        val tweetList: List<TweetBean> = TestUtils.generateFakeTweets(15)

        val pageCount = useCase(tweetList)

        assertEquals(3, pageCount)
    }
}
