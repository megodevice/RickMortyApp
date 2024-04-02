package com.iliazusik.rickmortyapp.ui.character

import androidx.lifecycle.ViewModel
import com.iliazusik.rickmortyapp.data.repository.CharacterRepository


class CharacterViewModel(private val repository: CharacterRepository) :
    ViewModel() {

    fun getCharacter(url: String) = repository.fetchCharacter(url)

    fun getEpisodes(urls: List<String>) = repository.fetchEpisodes(urls)

}