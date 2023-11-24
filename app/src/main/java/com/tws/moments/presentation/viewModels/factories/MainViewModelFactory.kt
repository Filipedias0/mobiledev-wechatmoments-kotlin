package com.tws.moments.presentation.viewModels.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tws.moments.data.repository.MomentRepository
import com.tws.moments.presentation.viewModels.MainViewModel
import kotlinx.coroutines.Dispatchers

class MainViewModelFactory(private val repository: MomentRepository) : ViewModelProvider.Factory {
    private val ioDispatcher = Dispatchers.Main
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        return MainViewModel(repository, ioDispatcher) as T
    }
}
