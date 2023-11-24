package com.tws.moments.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tws.moments.data.remote.api.MomentService
import com.tws.moments.presentation.viewModels.MainViewModel
import com.tws.moments.testUtils.MainDispatcherRule
import com.tws.moments.testUtils.TestUtils
import com.tws.moments.testUtils.getOrAwaitValue
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class ViewModelIntegrationtest {

    private lateinit var momentRepository: MomentRepository
    private lateinit var momentService: MomentService
    private lateinit var mainViewModel: MainViewModel


    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    var mainDispatcherRule: TestRule = MainDispatcherRule()

    @get:Rule
    val rule: TestRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        val dispatcher = Dispatchers.IO

        momentService = mockk()
        momentRepository = MomentRepository(momentService)
        mainViewModel = MainViewModel(momentRepository, dispatcher)
    }

    @Test
    fun `loadUserInfo should update userBean value on successful repository call`() = runTest {
        val fakeUser = TestUtils.generateFakeUser()

        coEvery { momentRepository.fetchUser() } returns fakeUser

        val result = mainViewModel.userBean.getOrAwaitValue()

        assertEquals(fakeUser, result)
    }

    @Test
    fun `loadUserInfo should not update userBean value on repository call failure`() = runTest {
        coEvery { momentRepository.fetchUser() } throws Exception()

        val userBean = mainViewModel.userBean.getOrAwaitValue()

        assertNull(userBean)
    }

    @Test
    fun `loadTweets should update tweets value on successful repository call`() = runTest {
        val fakeTweets = TestUtils.generateFakeTweets(1)

        coEvery { momentRepository.fetchTweets() } returns fakeTweets

        val result = mainViewModel.tweets.getOrAwaitValue()

        assertEquals(fakeTweets, result)
    }

    @Test
    fun `loadTweets should not update tweets value on repository call failure`() = runTest {
        coEvery { momentRepository.fetchTweets() } throws Exception()

        val tweets = mainViewModel.tweets.getOrAwaitValue()

        assertNull(tweets)
    }
}
