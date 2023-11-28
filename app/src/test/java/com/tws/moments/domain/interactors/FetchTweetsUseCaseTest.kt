package com.tws.moments.domain.interactors

import com.tws.moments.domain.repository.MomentRepository
import com.tws.moments.testUtils.TestUtils
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class FetchTweetsUseCaseTest {

    private lateinit var momentRepository: MomentRepository
    private lateinit var fetchTweetsUseCase: FetchTweetsUseCase

    @Before
    fun setUp() {
        momentRepository = mockk()
        fetchTweetsUseCase = FetchTweetsUseCase(momentRepository)
    }

    @Test
    fun `fetchTweets returns data when repository call is successful`() = runBlocking {
        val fakeTweets = TestUtils.generateFakeTweets(1)
        coEvery { momentRepository.fetchTweets() } returns fakeTweets

        val result = fetchTweetsUseCase()

        assertEquals(fakeTweets, result)
    }

    @Test
    fun `fetchTweets returns null when repository call fails`() = runBlocking {
        coEvery { momentRepository.fetchTweets() } throws Exception("Error fetching tweets")

        val result = fetchTweetsUseCase()

        assertNull(result)
    }
}
