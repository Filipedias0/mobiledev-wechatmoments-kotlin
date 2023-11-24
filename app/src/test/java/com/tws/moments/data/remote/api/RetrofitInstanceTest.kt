package com.tws.moments.data.remote.api

import com.tws.moments.data.remote.api.dto.TweetBean
import com.tws.moments.data.remote.api.dto.UserBean
import com.tws.moments.testUtils.TestUtils
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class MomentServiceTest {

    private lateinit var momentService: MomentService

    @Before
    fun setUp() {
        momentService = mockk()
    }

    @Test
    fun `test fetchUser`() = runBlocking {
        val mockedUser = TestUtils.generateFakeUser()

        coEvery { momentService.user("jsmith") } returns mockedUser

        val fetchedUser = momentService.user("jsmith")

        assertEquals(mockedUser, fetchedUser)
    }

    @Test
    fun `test fetchTweets`() = runBlocking {
        val mockedTweets = TestUtils.generateFakeTweets(1)
        coEvery { momentService.tweets("jsmith") } returns mockedTweets

        val fetchedTweets = momentService.tweets("jsmith")

        assertEquals(mockedTweets, fetchedTweets)
    }
}
