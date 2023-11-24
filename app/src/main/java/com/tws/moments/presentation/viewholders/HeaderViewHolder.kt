package com.tws.moments.presentation.viewholders

import androidx.recyclerview.widget.RecyclerView
import com.tws.moments.TWApplication
import com.tws.moments.data.remote.api.dto.UserBean
import com.tws.moments.databinding.ItemMomentHeadBinding

class HeaderViewHolder(private val binding: ItemMomentHeadBinding) :
    RecyclerView.ViewHolder(binding.root) {
    private var imageLoader = TWApplication.imageLoader

    fun bind(userBean: UserBean?) {
        with(binding){
            tvUserNickname.text = userBean?.nick
            imageLoader.displayImage(
                userBean?.avatar,
                ivUserAvatar
            )

            imageLoader.displayImage(
                userBean?.profileImage,
                ivUserProfile
            )
        }
    }
}