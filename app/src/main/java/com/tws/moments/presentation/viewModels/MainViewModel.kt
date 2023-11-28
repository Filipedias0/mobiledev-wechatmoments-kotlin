package com.tws.moments.presentation.viewModels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tws.moments.data.remote.api.dto.TweetBean
import com.tws.moments.data.remote.api.dto.UserBean
import com.tws.moments.domain.interactors.CalculatePageCountUseCase
import com.tws.moments.domain.interactors.FetchTweetsUseCase
import com.tws.moments.domain.interactors.FetchUserInfoUseCase
import com.tws.moments.domain.interactors.PaginateTweetsUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch

class MainViewModel(
    private val dispatcher: CoroutineDispatcher,
    private val calculatePageCountUseCase: CalculatePageCountUseCase,
    private val fetchTweetsUseCase: FetchTweetsUseCase,
    private val fetchUserInfoUseCase: FetchUserInfoUseCase,
    private val paginateTweetsUseCase: PaginateTweetsUseCase
) : ViewModel() {

    val userBean: MutableLiveData<UserBean> by lazy {
        MutableLiveData<UserBean>().also { loadUserInfo() }
    }

    val tweets: MutableLiveData<List<TweetBean>> by lazy {
        MutableLiveData<List<TweetBean>>().also { loadTweets() }
    }

    private var allTweets: List<TweetBean>? = null

    companion object{
        const val PAGE_TWEET_COUNT = 5
    }
    private fun loadUserInfo() {
        viewModelScope.launch(dispatcher) {
            val result = try {
                fetchUserInfoUseCase()
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
                fetchTweetsUseCase()
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }

            val filteredTweets = filterEmptyTweets(result)

            allTweets = filteredTweets

            if (!allTweets.isNullOrEmpty()) {
                tweets.value = paginateTweetsUseCase(allTweets, 0)
            } else {
                tweets.value = emptyList()
            }
        }
    }

    private fun filterEmptyTweets(tweets: List<TweetBean>?): MutableList<TweetBean>? {
        return tweets?.filter {
            it.content?.isNotEmpty() == true || it.images?.isNotEmpty() == true
        }?.toMutableList()
    }

    fun refreshTweets() {
        loadTweets()
    }

    val pageCount: Int
        get() = calculatePageCountUseCase(allTweets)

    fun loadMoreTweets(pageIndex: Int, onLoad: (List<TweetBean>?) -> Unit) {
        if (pageIndex < 0) {
            throw IllegalArgumentException("page index must be greater than or equal to 0.")
        }

        Log.d("moretweets", tweets.value?.size.toString())

        viewModelScope.launch(dispatcher) {
            val result = paginateTweetsUseCase(allTweets, pageIndex)
            onLoad(result)
        }
    }
}
