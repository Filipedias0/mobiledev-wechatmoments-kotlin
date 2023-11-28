package com.tws.moments.presentation.viewholders

import android.text.method.LinkMovementMethod
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.tws.moments.data.remote.api.dto.CommentBean
import com.tws.moments.databinding.ItemCommentBinding
import com.tws.moments.utils.clickableSpan

class CommentsViewHolder(private val binding: ItemCommentBinding) :
    RecyclerView.ViewHolder(binding.root){

    init {
        binding.tvSimpleComment.movementMethod = LinkMovementMethod.getInstance()
    }
    fun bind(commentsBean: CommentBean) {

        val spannableString = commentsBean.sender?.nick?.clickableSpan {
            Toast.makeText(it.context, "${commentsBean.sender.nick} info.", Toast.LENGTH_SHORT).show()
        }
        with(binding){

            tvSimpleComment.text = spannableString
            tvSimpleComment.append(":" + (commentsBean.content ?: ""))
        }
    }

}