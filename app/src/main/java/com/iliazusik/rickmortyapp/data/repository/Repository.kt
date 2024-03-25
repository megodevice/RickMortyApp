package com.iliazusik.rickmortyapp.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.iliazusik.rickmortyapp.data.Character
import com.iliazusik.rickmortyapp.data.CharactersModel
import com.iliazusik.rickmortyapp.data.network.CharacterApi
import com.iliazusik.rickmortyapp.utils.Resource
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class Repository @Inject constructor(
    private val api: CharacterApi
) {

    private val data = MutableLiveData<Resource<List<Character>>>()

    fun getData(): LiveData<Resource<List<Character>>> = data

    fun fetchCharacters() {

        data.postValue(Resource.Loading())

        api.getCharacters().enqueue(object : Callback<CharactersModel> {
            override fun onResponse(
                call: Call<CharactersModel>,
                response: Response<CharactersModel>
            ) {
                if (response.isSuccessful && response.body() != null && response.code() in 200..300) {
                    response.body()?.let {
                        data.postValue(Resource.Success(it.characters))
                    }
                }
            }

            override fun onFailure(c: Call<CharactersModel>, e: Throwable) {
                data.postValue(Resource.Error(e.localizedMessage ?: "Unknown error"))
            }
        })
    }

}