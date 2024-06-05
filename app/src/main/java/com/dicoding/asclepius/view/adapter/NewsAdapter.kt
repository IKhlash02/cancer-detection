package com.dicoding.asclepius.view.adapter
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.asclepius.R
import com.dicoding.asclepius.data.remote.response.ArticlesItem
import com.dicoding.asclepius.databinding.ItemNewsBinding
import com.dicoding.asclepius.helper.DateHelper

class NewsAdapter : ListAdapter<ArticlesItem, NewsAdapter.ViewHolder>(DIFF_CALLBACK) {

    class ViewHolder(private  val binding: ItemNewsBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(article: ArticlesItem){
            val date = DateHelper.convertToFormattedDate(article.publishedAt!!)
            val imageUrl = if (article.urlToImage.isNullOrEmpty()) {
                R.drawable.ic_place_holder
            } else {
                article.urlToImage
            }

                Glide.with(itemView.context)
                    .load(imageUrl)
                    .into(binding.imgItemPhoto)
                binding.tvDate.text = date
                binding.tvSource.text = article.source?.name
                binding.tvItemName.text = article.title

                binding.tvItemDescription.text = article.description

                itemView.setOnClickListener {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(article.url))
                   itemView.context.startActivity(intent)

                }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val articlesItem = getItem(position)
            holder.bind(articlesItem)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ArticlesItem>() {
            override fun areItemsTheSame(oldItem: ArticlesItem, newItem: ArticlesItem): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: ArticlesItem, newItem: ArticlesItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}