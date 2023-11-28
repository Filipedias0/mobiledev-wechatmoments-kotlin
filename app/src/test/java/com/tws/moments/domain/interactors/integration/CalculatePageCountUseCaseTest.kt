package com.tws.moments.domain.interactors.integration

import com.tws.moments.data.remote.api.MomentService
import com.tws.moments.data.remote.api.dto.TweetBean
import com.tws.moments.data.remote.api.dto.UserBean
import com.tws.moments.data.repository.MomentRepositoryImpl
import com.tws.moments.domain.interactors.CalculatePageCountUseCase
import com.tws.moments.testUtils.TestUtils
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.Mockito.mock

class CalculatePageCountIntegrationTest {

    private val momentService = mockk<MomentService>()
    private val momentRepository = MomentRepositoryImpl(momentService)
    private val calculatePageCountUseCase = CalculatePageCountUseCase()

    @Test
    fun calculatePageCountWithRepositoryShouldReturnCorrectCount() {
        val fakeTweets = TestUtils.generateFakeTweets(5)
        coEvery { momentService.tweets(any()) } returns fakeTweets

        val tweets: List<TweetBean> = runBlocking { momentRepository.fetchTweets() }

        val pageCount = calculatePageCountUseCase(tweets)

        assertEquals(1, pageCount)
    }
}
