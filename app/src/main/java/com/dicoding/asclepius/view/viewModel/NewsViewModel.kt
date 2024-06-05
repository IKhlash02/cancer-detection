package com.dicoding.asclepius.view.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.asclepius.data.remote.response.ArticlesItem
import com.dicoding.asclepius.data.remote.response.Response
import com.dicoding.asclepius.data.remote.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback

class NewsViewModel : ViewModel(){

    private val _article = MutableLiveData<List<ArticlesItem>>()
    val article = _article

    private val _showLoading = MutableLiveData<Boolean>()
    val showLoading = _showLoading

    init {
        showNews()
    }

    private fun showNews() {
        _showLoading.value = true
        val client = ApiConfig.getApiService().getListNews("cancer", "health","en", apiKey = "4f287d6f92f44c26a1ef55312bbb91f0")
        client.enqueue(object : Callback<Response> {
            override fun onResponse(call: Call<Response>, response: retrofit2.Response<Response>) {
                _showLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _article.value = responseBody.articles
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<Response>, t: Throwable) {
                _showLoading.value = false
                Log.e(TAG, "onFailure: $t")
            }

        })
    }

    companion object {
        private const val TAG = "NewsViewModel"
    }
}