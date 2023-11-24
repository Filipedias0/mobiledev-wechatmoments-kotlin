package com.tws.moments.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tws.moments.data.remote.api.MomentService
import com.tws.moments.data.remote.api.dto.UserBean
import com.tws.moments.testUtils.MainDispatcherRule
import com.tws.moments.testUtils.TestUtils
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class MomentRepositoryTest {

    private lateinit var momentRepository: MomentRepository
    private lateinit var momentService: MomentService

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    var mainDispatcherRule: TestRule = MainDispatcherRule()

    @get:Rule
    val rule: TestRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        momentService = mockk()
        momentRepository = MomentRepository(momentService)
    }

    @Test
    fun `test fetchUser`() = runBlocking {
        val user = TestUtils.generateFakeUser()

        coEvery { momentService.user("jsmith") } returns user

        val fetchedUser = momentRepository.fetchUser()

        assertEquals(user, fetchedUser)
    }

    @Test
    fun `test fetchTweets`() = runBlocking {
        val tweets = TestUtils.generateFakeTweets(1)

        coEvery { momentService.tweets("jsmith") } returns tweets

        val fetchedTweets = momentRepository.fetchTweets()

        assertEquals(tweets, fetchedTweets)
    }
}
