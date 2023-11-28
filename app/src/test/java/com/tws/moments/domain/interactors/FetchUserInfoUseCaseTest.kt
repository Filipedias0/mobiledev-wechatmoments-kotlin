package com.tws.moments.domain.interactors

import com.tws.moments.domain.repository.MomentRepository
import com.tws.moments.testUtils.TestUtils
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class FetchUserInfoUseCaseTest {

    private lateinit var momentRepository: MomentRepository
    private lateinit var fetchUserInfoUseCase: FetchUserInfoUseCase

    @Before
    fun setUp() {
        momentRepository = mockk()
        fetchUserInfoUseCase = FetchUserInfoUseCase(momentRepository)
    }

    @Test
    fun `fetchUserInfo returns data when repository call is successful`() = runBlocking {
        val fakeUser = TestUtils.generateFakeUser()
        coEvery { momentRepository.fetchUser() } returns fakeUser

        val result = fetchUserInfoUseCase()

        assertEquals(fakeUser, result)
    }

    @Test
    fun `fetchUserInfo returns null when repository call fails`() = runBlocking {
        coEvery { momentRepository.fetchUser() } throws Exception("Error fetching user info")

        val result = fetchUserInfoUseCase()

        assertNull(result)
    }
}
