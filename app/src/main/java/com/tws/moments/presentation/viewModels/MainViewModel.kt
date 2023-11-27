package com.tws.moments.presentation.viewModels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tws.moments.data.repository.MomentRepositoryImpl
import com.tws.moments.data.remote.api.dto.TweetBean
import com.tws.moments.data.remote.api.dto.UserBean
import com.tws.moments.domain.repository.MomentRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlin.math.min

class MainViewModel(
    private val repository: MomentRepository,
    private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    private  val TAG = "MainViewModel##"

    private  val PAGE_TWEET_COUNT = 5

    val userBean: MutableLiveData<UserBean> by lazy {
        MutableLiveData<UserBean>().also { loadUserInfo() }
    }

    val tweets: MutableLiveData<List<TweetBean>> by lazy {
        MutableLiveData<List<TweetBean>>().also { loadTweets() }
    }

    private var allTweets: List<TweetBean>? = null


    private fun loadUserInfo() {
        viewModelScope.launch(dispatcher) {
            val result = try {
                repository.fetchUser()
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
            userBean.value = result
        }
    }


    private fun loadTweets() {
        viewModelScope.launch(dispatcher) {
            val result = try {
                repository.fetchTweets()
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }

            if (result != null) {
                Log.d("loadTweets", result[0].content.toString())
            }

            allTweets = result

            if ((allTweets?.size ?: 0) > PAGE_TWEET_COUNT) {
                tweets.value = allTweets?.subList(0, PAGE_TWEET_COUNT)
            } else {
                tweets.value = allTweets
            }
        }
    }

    fun refreshTweets() {
        loadTweets()
    }

    val pageCount: Int
        get() {
            return when {
                allTweets.isNullOrEmpty() -> 0
                allTweets!!.size % PAGE_TWEET_COUNT == 0 -> allTweets!!.size / PAGE_TWEET_COUNT
                else -> allTweets!!.size / PAGE_TWEET_COUNT + 1
            }
        }

    fun loadMoreTweets(pageIndex: Int, onLoad: (List<TweetBean>?) -> Unit) {
        if (pageIndex < 0) {
            throw IllegalArgumentException("page index must greater than or equal to 0.")
        }

        if (pageIndex > pageCount - 1) {
            return
        }

        viewModelScope.launch(dispatcher) {
            val startIndex = PAGE_TWEET_COUNT * pageIndex
            val endIndex = min(allTweets!!.size, PAGE_TWEET_COUNT * (pageIndex + 1))
            val result = allTweets!!.subList(startIndex, endIndex)
            onLoad(result)
        }
    }

}
