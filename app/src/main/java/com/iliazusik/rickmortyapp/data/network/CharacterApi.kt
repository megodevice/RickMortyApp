package com.iliazusik.rickmortyapp.data.network

import com.ilia_zusik.rickmortyapp.BuildConfig
import com.iliazusik.rickmortyapp.data.models.Character
import com.iliazusik.rickmortyapp.data.models.Characters
import com.iliazusik.rickmortyapp.data.models.Episode
import com.iliazusik.rickmortyapp.data.models.Episodes
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface CharacterApi {

    @GET(BuildConfig.CHARACTER)
    suspend fun getCharactersByPage(
        @Query(BuildConfig.PAGE) page: String
    ): Response<Characters>

    @GET
    suspend fun getEpisode(@Url url: String): Response<Episode>

    @GET
    suspend fun getEpisodes(@Url url: String): Response<Episodes>

    @GET
    suspend fun getCharacter(@Url url: String): Response<Character>

}