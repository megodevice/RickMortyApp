package com.iliazusik.rickmortyapp.ui.characters

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.iliazusik.rickmortyapp.data.Character
import com.iliazusik.rickmortyapp.data.repository.CharactersRepository


class CharactersViewModel(repository: CharactersRepository) : ViewModel() {

    val characterList: LiveData<PagingData<Character>> =
        repository.getCharacters()
            .cachedIn(viewModelScope)

}