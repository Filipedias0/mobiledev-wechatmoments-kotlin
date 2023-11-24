package com.tws.moments.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tws.moments.data.remote.api.dto.CommentBean
import com.tws.moments.databinding.ItemCommentBinding
import com.tws.moments.presentation.viewholders.CommentsViewHolder

class CommentsAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var comments: List<CommentBean>? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return CommentsViewHolder(
            ItemCommentBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return comments?.size ?: 0
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as? CommentsViewHolder)?.bind(comments!![position])
    }
}
