package com.iliazusik.rickmortyapp.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.iliazusik.rickmortyapp.data.Character
import com.iliazusik.rickmortyapp.data.CharactersModel
import com.iliazusik.rickmortyapp.network.CharacterApi
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class CharacterViewModel @Inject constructor(private val characterApi: CharacterApi) : ViewModel() {

    private var page: Int = 0
    private var maxPage: Int = 0
    private val _characters: MutableLiveData<List<Character>> = MutableLiveData(listOf())
    private val _message: MutableLiveData<String> = MutableLiveData(String())
    val characters: LiveData<List<Character>> = _characters
    val message: LiveData<String> = _message

    fun giveCharacters() {
        if (page <= maxPage)
            characterApi.getCharacters(page).enqueue(object : Callback<CharactersModel> {
                override fun onResponse(p0: Call<CharactersModel>, p1: Response<CharactersModel>) {
                    if (p1.isSuccessful) {
                        p1.body()?.apply {
                            if (this.characters.isNotEmpty()) {
                                _characters.postValue(
                                    _characters.value?.toMutableList()
                                        ?.apply { addAll(p1.body()?.characters ?: listOf()) }
                                )
                            }
                            maxPage = this.requestInfo.pages
                            if (page == 0) {
                                _message.postValue("Total characters: ${this.requestInfo.count}")
                            }
                            page++
                        }
                    }
                }

                override fun onFailure(p0: Call<CharactersModel>, p1: Throwable) {
                    _message.postValue(p1.message ?: "Error")
                }
            })
    }
}