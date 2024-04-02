package com.iliazusik.rickmortyapp.data.network

import com.ilia_zusik.rickmortyapp.BuildConfig
import com.iliazusik.rickmortyapp.data.Character
import com.iliazusik.rickmortyapp.data.CharactersModel
import com.iliazusik.rickmortyapp.data.Episode
import com.iliazusik.rickmortyapp.data.EpisodesModel
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
    suspend fun getSingleEpisode(@Url url: String): Response<Episode>

    @GET
    suspend fun getMultiplyEpisode(@Url url: String): Response<EpisodesModel>

    @GET
    suspend fun getSingleCharacter(@Url url: String): Response<Character>

}