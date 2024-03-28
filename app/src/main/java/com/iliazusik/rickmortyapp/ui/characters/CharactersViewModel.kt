package com.iliazusik.rickmortyapp.ui.characters

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.iliazusik.rickmortyapp.data.Character
import com.iliazusik.rickmortyapp.data.repository.CharactersRepository
import com.iliazusik.rickmortyapp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CharactersViewModel @Inject constructor(private val repository: CharactersRepository) : ViewModel() {

    val characters: LiveData<Resource<List<Character>>> = repository.getData()

    fun getCharacters() {
        repository.fetchCharacters()
    }

}