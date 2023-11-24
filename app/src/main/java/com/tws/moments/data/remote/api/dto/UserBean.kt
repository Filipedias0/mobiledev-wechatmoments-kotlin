package com.tws.moments.data.remote.api.dto

import com.google.gson.annotations.SerializedName

data class UserBean(
    var username: String? = null,
    @SerializedName("profile-image")
    val profileImage : String? = null,
    val avatar : String? = null,
    val nick : String? = null,
)