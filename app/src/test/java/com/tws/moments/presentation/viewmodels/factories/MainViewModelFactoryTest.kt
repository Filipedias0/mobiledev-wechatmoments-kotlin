package com.tws.moments.presentation.viewmodels.factories

import com.tws.moments.data.repository.MomentRepository
import com.tws.moments.presentation.viewModels.MainViewModel
import com.tws.moments.presentation.viewModels.factories.MainViewModelFactory
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Test

class MainViewModelFactoryTest {

    private val mockRepository: MomentRepository = mockk()
    private val factory = MainViewModelFactory(mockRepository)

    @Test
    fun `test MainViewModelFactory creates MainViewModel`() {
        val createdViewModel = factory.create(MainViewModel::class.java)

        assertEquals(MainViewModel::class.java, createdViewModel::class.java)
    }
}
