package com.iliazusik.rickmortyapp.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.iliazusik.rickmortyapp.data.Character
import com.iliazusik.rickmortyapp.data.Episode
import com.iliazusik.rickmortyapp.data.EpisodesModel
import com.iliazusik.rickmortyapp.data.network.CharacterApi
import com.iliazusik.rickmortyapp.utils.Resource
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class CharacterRepository(
    private val api: CharacterApi
) {

    private val character = MutableLiveData<Resource<Character>>()
    private val episodes = MutableLiveData<Resource<List<Episode>>>()

    fun getCharacter(): LiveData<Resource<Character>> = character
    fun getEpisodes(): LiveData<Resource<List<Episode>>> = episodes

    fun fetchCharacter(url: String) {
        character.postValue(Resource.Loading())

        api.getSingleCharacter(url).enqueue(object : Callback<Character> {
            override fun onResponse(
                call: Call<Character>, response: Response<Character>
            ) {
                if (response.isSuccessful && response.body() != null && response.code() in 200..300) {
                    response.body()?.let { result ->
                        character.postValue(Resource.Success(result))
                    }
                }
            }

            override fun onFailure(c: Call<Character>, e: Throwable) {
                character.postValue(Resource.Error(e.localizedMessage ?: "Unknown error"))
            }
        })
    }

    fun fetchEpisodes(urls: List<String>) {
        episodes.postValue(Resource.Loading())

        if (urls.isNotEmpty()) {
            var sumEpisodesUrl: String = urls[0]
            if (urls.size > 1) {
                var episodesNumbers = String()
                for (url in urls) {
                    if (url != sumEpisodesUrl) {
                        var temp: String? = null
                        try {
                            temp = url.subSequence(url.lastIndexOf('/') + 1, url.length).toString()
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
                api.getMultiplyEpisode(sumEpisodesUrl).enqueue(object : Callback<EpisodesModel> {
                    override fun onResponse(
                        p0: Call<EpisodesModel>,
                        response: Response<EpisodesModel>
                    ) {
                        if (response.isSuccessful && response.body() != null && response.code() in 200..300) {
                            response.body()?.let { result ->
                                episodes.postValue(Resource.Success(result))
                            }
                        }
                    }

                    override fun onFailure(p0: Call<EpisodesModel>, e: Throwable) {
                        episodes.postValue(Resource.Error(e.localizedMessage ?: "Unknown error"))
                    }
                })
            } else {
                api.getSingleEpisode(sumEpisodesUrl).enqueue(object : Callback<Episode> {
                    override fun onResponse(
                        episodeCall: Call<Episode>,
                        episodeResponse: Response<Episode>
                    ) {
                        if (episodeResponse.isSuccessful && episodeResponse.body() != null && episodeResponse.code() in 200..300) {
                            episodeResponse.body()?.let { episode ->
                                episodes.postValue(Resource.Success(listOf(episode)))
                            }
                        }
                    }

                    override fun onFailure(p0: Call<Episode>, e: Throwable) {
                        episodes.postValue(Resource.Error(e.localizedMessage ?: "Unknown error"))
                    }
                })
            }
        }
    }
}