package com.iliazusik.rickmortyapp.data.repository

import androidx.lifecycle.LiveDataScope
import com.iliazusik.rickmortyapp.utils.Resource
import retrofit2.Response

object BaseRepository {
    fun <T> getResource(response: Response<T>): Resource<T> {
        return if (response.isSuccessful && response.body() != null && response.code() in 200..300)
            Resource.Success(response.body()!!)
        else
            Resource.Error("Server error")
    }


    suspend fun <T> makeRequest(scope: LiveDataScope<Resource<T>>, requestFun: suspend (String) -> Response<T>, url: String) {
        scope.run {
            emit(Resource.Loading())
            try {
                emit(getResource(requestFun(url)))
            } catch (e: Exception) {
                emit(Resource.Error(e.localizedMessage ?: "Unknown error"))
            }
        }
    }

    fun convertMultiplyUrl(urls: List<String>) : String {
        var sumEpisodesUrl: String = urls[0]
        if (urls.size > 1) {
            var episodesNumbers = String()
            for (url in urls) {
                if (url != sumEpisodesUrl) {
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
            sumEpisodesUrl += episodesNumbers
        }
        return sumEpisodesUrl
    }
}