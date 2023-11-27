package com.tws.moments.domain.interactors

import com.tws.moments.data.remote.api.dto.UserBean
import com.tws.moments.domain.repository.MomentRepository

class FetchUserInfoUseCase(private val repository: MomentRepository) {

    suspend operator fun invoke(): UserBean? {
        return try {
            repository.fetchUser()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
