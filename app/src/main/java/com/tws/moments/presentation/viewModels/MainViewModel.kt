package com.tws.moments.presentation.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tws.moments.data.remote.api.dto.TweetBean
import com.tws.moments.data.remote.api.dto.UserBean
import com.tws.moments.domain.interactors.CalculatePageCountUseCase
import com.tws.moments.domain.interactors.FetchTweetsUseCase
import com.tws.moments.domain.interactors.FetchUserInfoUseCase
import com.tws.moments.domain.interactors.PaginateTweetsUseCase
import com.tws.moments.domain.repository.MomentRepository
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
        const val PAGE_TWEET_COUNT = 10
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
        get() = calculatePageCountUseCase(allTweets)

    fun loadMoreTweets(pageIndex: Int, onLoad: (List<TweetBean>?) -> Unit) {
        if (pageIndex < 0) {
            throw IllegalArgumentException("page index must be greater than or equal to 0.")
        }

        if (pageIndex > pageCount - 1) {
            return
        }

        viewModelScope.launch(dispatcher) {
            val startIndex = PAGE_TWEET_COUNT * pageIndex
            val endIndex = allTweets?.size?.coerceAtMost(
                PAGE_TWEET_COUNT * (pageIndex + 1)
            ) ?: 0
            val result = allTweets?.subList(startIndex, endIndex)
            onLoad(result)
        }
    }
}
