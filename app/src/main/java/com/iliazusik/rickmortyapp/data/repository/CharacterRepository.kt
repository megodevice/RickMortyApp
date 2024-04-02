package com.iliazusik.rickmortyapp.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.iliazusik.rickmortyapp.data.Character
import com.iliazusik.rickmortyapp.data.EpisodesModel
import com.iliazusik.rickmortyapp.data.network.CharacterApi
import com.iliazusik.rickmortyapp.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlin.Exception

class CharacterRepository(
    private val api: CharacterApi
) {
    fun fetchCharacter(url: String): LiveData<Resource<Character>> {
        return liveData(Dispatchers.IO) {
            emit(Resource.Loading())
            try {
                emit(BaseRepository.getResult(api.getSingleCharacter(url)))
            } catch (e: Exception) {
                emit(Resource.Error(e.localizedMessage ?: "Unknown error"))
            }
        }
    }


    fun fetchEpisodes(urls: List<String>): LiveData<Resource<EpisodesModel>> {
        return liveData(Dispatchers.IO) {
            if (urls.isNotEmpty()) {
                val sumEpisodesUrl = BaseRepository.convertMultiplyUrl(urls)
                if (urls.size > 1) {
                    emit(Resource.Loading())
                    try {
                        emit(BaseRepository.getResult(api.getMultiplyEpisode(sumEpisodesUrl)))
                    } catch (e: Exception) {
                        emit(Resource.Error(e.localizedMessage ?: "Unknown error"))
                    }
                } else {
                    emit(Resource.Loading())
                    try {
                        BaseRepository.getResult(api.getSingleEpisode(sumEpisodesUrl)).let {
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