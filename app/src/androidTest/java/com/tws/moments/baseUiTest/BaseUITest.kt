package com.tws.moments.baseUiTest

import com.google.gson.Gson
import com.tws.moments.testUtils.TestUtils
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest

abstract class BaseUITest : KoinTest {


    companion object {
        lateinit var mockServer: MockWebServer
    }

    @Before
    open fun setUp() {
        startMockServer()
    }

    fun mockNetworkResponse() {
        val userResponse = TestUtils.generateFakeUser()
        val tweetResponse = TestUtils.generateFakeTweets(5)

        mockServer.enqueue(MockResponse().setResponseCode(200).setBody(Gson().toJson(userResponse)))
        mockServer.enqueue(MockResponse().setResponseCode(200).setBody(Gson().toJson(tweetResponse)))
    }


    private fun startMockServer() {
        mockServer = MockWebServer()
        mockServer.start()
    }

    fun getMockWebServerUrl() = mockServer.url("/").toString()

    @After
    open fun tearDown() {
        mockServer.shutdown()
        stopKoin()
    }
}
