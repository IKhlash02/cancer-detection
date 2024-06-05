package com.dicoding.asclepius.view.viewModel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.dicoding.asclepius.data.local.entity.History
import com.dicoding.asclepius.data.remote.response.ArticlesItem
import com.dicoding.asclepius.repository.HistoryRepository

class HistoryViewModel(aplication: Application): ViewModel() {
    private val mHistoryRepository: HistoryRepository = HistoryRepository(aplication)

    fun getAllHistory(): LiveData<List<History>> = mHistoryRepository.getAllHistory()

}