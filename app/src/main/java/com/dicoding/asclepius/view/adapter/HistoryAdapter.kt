package com.dicoding.asclepius.view.adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.asclepius.data.local.entity.History
import com.dicoding.asclepius.databinding.ItemHistoryBinding
import com.dicoding.asclepius.view.ui.MainActivity
import com.dicoding.asclepius.view.ui.ResultActivity

class HistoryAdapter : ListAdapter<History, HistoryAdapter.ViewHolder>(DIFF_CALLBACK) {

    class ViewHolder(private  val binding: ItemHistoryBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(history: History){
            Glide.with(itemView.context)
                .load(Uri.parse(history.imageUri))
                .into(binding.imgItemPhoto)

            binding.tvDate.text = history.date
            binding.tvItemName.text = history.result
           

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val historyItem = getItem(position)
        holder.bind(historyItem)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<History>() {
            override fun areItemsTheSame(oldItem: History, newItem: History): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: History, newItem: History): Boolean {
                return oldItem == newItem
            }
        }
    }
}