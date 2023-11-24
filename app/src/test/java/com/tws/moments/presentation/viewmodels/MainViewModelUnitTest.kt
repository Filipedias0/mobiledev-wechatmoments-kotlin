package com.tws.moments.presentation.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tws.moments.data.repository.MomentRepository
import com.tws.moments.data.remote.api.dto.TweetBean
import com.tws.moments.presentation.viewModels.MainViewModel
import com.tws.moments.testUtils.MainDispatcherRule
import com.tws.moments.testUtils.TestUtils
import com.tws.moments.testUtils.getOrAwaitValue
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.*
import org.junit.rules.TestRule

class MainViewModelUnitTest {

    private val PAGE_TWEET_COUNT = 5

    private lateinit var mainViewModel: MainViewModel
    private lateinit var momentRepository: MomentRepository

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    var mainDispatcherRule: TestRule = MainDispatcherRule()

    @get:Rule
    val rule: TestRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        val dispatcher = Dispatchers.IO

        momentRepository = mockk()
        mainViewModel = MainViewModel(momentRepository, dispatcher)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun testFetchTweets() = runTest {
        val fakeTweets: List<TweetBean> = TestUtils.generateFakeTweets(1)

        coEvery { momentRepository.fetchTweets() } returns fakeTweets

        mainViewModel.refreshTweets()

        val result = mainViewModel.tweets.getOrAwaitValue()
        assertEquals(fakeTweets, result)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun testPageCount_withEmptyTweets() = runTest {
        mainViewModel.refreshTweets()

        mainViewModel.tweets.getOrAwaitValue {
            assertEquals(0, mainViewModel.pageCount)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun testPageCount_withOnePage() = runTest {
        val fakeTweets: List<TweetBean> = TestUtils.generateFakeTweets(PAGE_TWEET_COUNT)

        coEvery { momentRepository.fetchTweets() } returns fakeTweets

        mainViewModel.refreshTweets()

        mainViewModel.tweets.getOrAwaitValue {
            assertEquals(1, mainViewModel.pageCount)
        }
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `loadUserInfo should update userBean value on successful repository call`() = runTest {
        val fakeUser = TestUtils.generateFakeUser()

        coEvery { momentRepository.fetchUser() } returns fakeUser

        val teste = mainViewModel.userBean.getOrAwaitValue()

        assertEquals(fakeUser, teste)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `loadUserInfo should not update userBean value on repository call failure`() = runTest {
        coEvery { momentRepository.fetchUser() } throws Exception()

        val userBean = mainViewModel.userBean.getOrAwaitValue()

        assertNull(userBean)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `test page count with number of tweets divisible by 5`() = runTest {
        val fakeTweets: List<TweetBean> = TestUtils.generateFakeTweets(10)

        coEvery { momentRepository.fetchTweets() } returns fakeTweets

        mainViewModel.tweets.getOrAwaitValue()

        assertEquals(2, mainViewModel.pageCount)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `testLoadMoreTweets with invalid pageIndex`() {
        mainViewModel.loadMoreTweets(-1){
        }
        val tweets = mainViewModel.tweets.getOrAwaitValue()

        assertNull(tweets)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `testLoadMoreTweets with index greater than pageIndex`() = runTest{
        val fakeTweets: List<TweetBean> = TestUtils.generateFakeTweets(1)

        coEvery { momentRepository.fetchTweets() } returns fakeTweets

        val tweets = mainViewModel.tweets.getOrAwaitValue()

        mainViewModel.loadMoreTweets(5){
        }


        assertEquals(fakeTweets, tweets)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `test page count with number of tweets not divisible by 5`() = runTest {
        val fakeTweets: List<TweetBean> = TestUtils.generateFakeTweets(11)

        coEvery { momentRepository.fetchTweets() } returns fakeTweets

        mainViewModel.tweets.getOrAwaitValue()

        assertEquals(3, mainViewModel.pageCount)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `testLoadMoreTweets with valid index`() = runTest{
        val fakeTweets: List<TweetBean> = TestUtils.generateFakeTweets(1)

        coEvery { momentRepository.fetchTweets() } returns fakeTweets

        val tweets = mainViewModel.tweets.getOrAwaitValue()

        mainViewModel.loadMoreTweets(0){
        }

        assertEquals(fakeTweets, tweets)
    }

}
