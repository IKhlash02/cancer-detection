package com.dicoding.asclepius.view.viewModel

import android.app.Application
import androidx.lifecycle.ViewModel
import com.dicoding.asclepius.data.local.entity.History
import com.dicoding.asclepius.repository.HistoryRepository

class MainViewModel(aplication : Application) : ViewModel() {
    private val mHistoryRepository: HistoryRepository = HistoryRepository(aplication)

    fun insert(history: History){
        mHistoryRepository.insert(history)
    }
}