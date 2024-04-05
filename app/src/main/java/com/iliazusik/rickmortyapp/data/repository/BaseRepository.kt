package com.iliazusik.rickmortyapp.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.iliazusik.rickmortyapp.utils.Resource
import kotlinx.coroutines.Dispatchers
import retrofit2.Response

abstract class BaseRepository {
    protected fun <T> doRequest(requestFun: suspend () -> Response<T>): LiveData<Resource<T>> {
        return liveData(Dispatchers.IO) {
            emit(Resource.Loading())
            try {
                val response = requestFun.invoke()
                if (response.isSuccessful && response.body() != null && response.code() in 200..300)
                    emit(Resource.Success(response.body()!!))
                else
                    emit(Resource.Error("Server error"))
            } catch (e: Exception) {
                emit(Resource.Error(e.localizedMessage ?: "Unknown error"))
            }
        }
    }

    fun convertUrls(urls: List<String>): String {
        var sumUrl: String = urls[0]
        if (urls.size > 1) {
            var episodesNumbers = String()
            for (url in urls) {
                if (url != sumUrl) {
                    var temp: String? = null
                    try {
                        temp =
                            url.subSequence(url.lastIndexOf('/') + 1, url.length).toString()
                    } catch (_: Exception) {
                    }
                    temp?.apply {
                        if (this.isNotEmpty()) {
                            episodesNumbers += ",$this"
                        }
                    }
                }
            }
            sumUrl += episodesNumbers
        }
        return sumUrl
    }
}