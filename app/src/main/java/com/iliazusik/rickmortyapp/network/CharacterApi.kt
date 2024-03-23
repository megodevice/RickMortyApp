package com.iliazusik.rickmortyapp.network

import com.ilia_zusik.rickmortyapp.BuildConfig
import com.iliazusik.rickmortyapp.data.CharactersModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface CharacterApi {

    @GET(BuildConfig.CHARACTER)
    fun getCharacters(@Query(BuildConfig.PAGE) page: Int) : Call<CharactersModel>

}