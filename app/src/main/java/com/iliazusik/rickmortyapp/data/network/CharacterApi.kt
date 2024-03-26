package com.iliazusik.rickmortyapp.data.network

import com.ilia_zusik.rickmortyapp.BuildConfig
import com.iliazusik.rickmortyapp.data.CharactersModel
import com.iliazusik.rickmortyapp.data.EpisodeModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url

interface CharacterApi {

    @GET(BuildConfig.CHARACTER)
    fun getCharacters() : Call<CharactersModel>

    @GET
    fun getSingleEpisode(@Url url: String) : Call<EpisodeModel>

}