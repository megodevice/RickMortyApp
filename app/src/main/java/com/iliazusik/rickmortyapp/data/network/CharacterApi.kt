package com.iliazusik.rickmortyapp.data.network

import com.ilia_zusik.rickmortyapp.BuildConfig
import com.iliazusik.rickmortyapp.data.Character
import com.iliazusik.rickmortyapp.data.CharactersModel
import com.iliazusik.rickmortyapp.data.Episode
import com.iliazusik.rickmortyapp.data.EpisodesModel
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface CharacterApi {

    @GET(BuildConfig.CHARACTER)
    suspend fun getCharactersByPage(
        @Query(BuildConfig.PAGE) page: String
    ): Response<CharactersModel>

    @GET
    fun getSingleEpisode(@Url url: String): Call<Episode>

    @GET
    fun getMultiplyEpisode(@Url url: String): Call<EpisodesModel>

    @GET
    fun getSingleCharacter(@Url url: String): Call<Character>

}