package com.tws.moments

import BaseUITest
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.google.gson.Gson
import com.tws.moments.data.remote.api.MomentService
import com.tws.moments.data.remote.api.dto.UserBean
import com.tws.moments.di.generateTestAppComponent
import com.tws.moments.testUtils.TestUtils
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.loadKoinModules
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


@LargeTest
@RunWith(AndroidJUnit4::class)
class MainActivityTest : BaseUITest() {

    @get:Rule
    var activityRule: ActivityScenarioRule<MainActivity> =
        ActivityScenarioRule(MainActivity::class.java)

    @Before
    fun start() {
        super.setUp()
        loadKoinModules(generateTestAppComponent(mockServer).toMutableList())
        mockNetworkResponse()
    }

    @Test
    fun checkRecyclerViewVisibility() {
        Espresso.onView(ViewMatchers.withId(R.id.recyclerView))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun checkItemsLoadedInRecyclerView() {

        Thread.sleep(10000)

        val expectedItemCount = 4

        Espresso.onView(ViewMatchers.withId(R.id.recyclerView))
            .check(RecyclerViewItemCountAssertion(expectedItemCount))
    }
}
