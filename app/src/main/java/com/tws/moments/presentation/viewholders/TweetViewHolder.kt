package com.tws.moments.presentation.viewholders

import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tws.moments.TWApplication
import com.tws.moments.presentation.adapters.CommentsAdapter
import com.tws.moments.presentation.adapters.ImagesAdapter
import com.tws.moments.data.remote.api.dto.CommentBean
import com.tws.moments.data.remote.api.dto.ImageBean
import com.tws.moments.data.remote.api.dto.SenderBean
import com.tws.moments.data.remote.api.dto.TweetBean
import com.tws.moments.databinding.LayoutBaseTweetBinding
import com.tws.moments.utils.dip
import com.tws.moments.presentation.views.itemdecoration.ImagesDecoration
import com.tws.moments.presentation.views.itemdecoration.MarginItemDecoration

private const val IMAGE_SPAN_COUNT = 3

class TweetViewHolder(private val binding: LayoutBaseTweetBinding) :
    RecyclerView.ViewHolder(binding.root) {

    init {
        setupCommentsView()
        addTweetImagesView()
    }

    private lateinit var imagesAdapter: ImagesAdapter
    private lateinit var commentsAdapter: CommentsAdapter
    private var imageLoader = TWApplication.imageLoader
    fun bind(tweet: TweetBean) {
        renderTextContent(tweet.content)
        renderImages(tweet.images)

        renderSenderAvatar(tweet.sender?.avatar)
        renderSenderInfo(tweet.sender)

        renderComments(tweet.comments)
    }

    private fun renderTextContent(content: String?) {
        binding.tvTweetContent.text = content
    }

    private fun renderSenderInfo(sender: SenderBean?) {
        renderSenderAvatar(sender?.avatar)
        renderSenderNickname(sender?.nick)
    }

    private fun renderSenderAvatar(avatar: String?) {
        imageLoader.displayImage(
            avatar,
            binding.ivSenderAvatar
        )
    }

    private fun renderSenderNickname(nick: String?) {
        binding.tvSenderNickname.text = nick
    }
    private fun renderImages(imageBean: List<ImageBean>?) {
        if (imageBean.isNullOrEmpty()) {
            binding.simpleImageView.visibility = View.GONE
            binding.imagesRecyclerView.visibility = View.GONE
            return
        }
        binding.imagesRecyclerView.layoutManager = if (imageBean.size == 4) {
            GridLayoutManager(itemView.context, 2, RecyclerView.HORIZONTAL, false)
        } else {
            GridLayoutManager(itemView.context, IMAGE_SPAN_COUNT, RecyclerView.VERTICAL, false)
        }

        if (imageBean.size == 1) {
            binding.simpleImageView.visibility = View.VISIBLE
            binding.imagesRecyclerView.visibility = View.GONE
            val url = imageBean[0].url
            imageLoader.displayImage(
                url, binding.simpleImageView
            )
            binding.simpleImageView.tag = url
            imagesAdapter.images = null
        } else {
            binding.simpleImageView.visibility = View.GONE
            binding.imagesRecyclerView.visibility = View.VISIBLE
            imagesAdapter.images =
                imageBean.asSequence().map { it.url ?: "" }.filter { it.isNotEmpty() }.toList()
        }
    }

    private fun renderComments(comments: List<CommentBean>?) {
        commentsAdapter.comments = comments
    }

    private fun setupCommentsView() {
        with(binding.rvComments){

            layoutManager = LinearLayoutManager(itemView.context)
            commentsAdapter = CommentsAdapter()
            adapter = commentsAdapter

            addItemDecoration(
                MarginItemDecoration(
                    RecyclerView.VERTICAL,
                    itemView.context.dip(5)
                )
            )
        }
    }

    private fun addTweetImagesView() {
        imagesAdapter = ImagesAdapter()
        binding.imagesRecyclerView.addItemDecoration(ImagesDecoration(itemView.context.dip(5)))
        binding.imagesRecyclerView.adapter = imagesAdapter
    }
}