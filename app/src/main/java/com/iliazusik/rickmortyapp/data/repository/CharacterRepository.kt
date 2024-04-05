package com.iliazusik.rickmortyapp.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.iliazusik.rickmortyapp.data.EpisodesModel
import com.iliazusik.rickmortyapp.data.network.CharacterApi
import com.iliazusik.rickmortyapp.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlin.Exception

class CharacterRepository(
    private val api: CharacterApi
) : BaseRepository() {
    fun fetchCharacter(url: String) = doRequest { api.getSingleCharacter(url) }


    fun fetchEpisodes(urls: List<String>): LiveData<Resource<EpisodesModel>> {
        return liveData(Dispatchers.IO) {
            if (urls.isNotEmpty()) {
                val sumEpisodesUrl = convertMultiplyUrl(urls)
                if (urls.size > 1) {
                    makeRequest(this) {
                        api.getMultiplyEpisode(sumEpisodesUrl)
                    }
                } else {
                    emit(Resource.Loading())
                    try {
                        getResource(api.getSingleEpisode(sumEpisodesUrl)).let {
                            if (it is Resource.Success) {
                                emit(Resource.Success(EpisodesModel().apply { add(it.data!!) }))
                            }
                            if (it is Resource.Error) {
                                emit(Resource.Error(it.message!!))
                            }
                        }
                    } catch (e: Exception) {
                        emit(Resource.Error(e.localizedMessage ?: "Unknown error"))
                    }
                }
            } else {
                emit(Resource.Error("There are no episodes"))
            }
        }
    }
}