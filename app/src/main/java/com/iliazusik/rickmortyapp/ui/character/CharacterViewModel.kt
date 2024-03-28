package com.iliazusik.rickmortyapp.ui.character

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.iliazusik.rickmortyapp.data.Character
import com.iliazusik.rickmortyapp.data.Episode
import com.iliazusik.rickmortyapp.data.repository.CharacterRepository
import com.iliazusik.rickmortyapp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CharacterViewModel @Inject constructor(private val repository: CharacterRepository) :
    ViewModel() {

    val character: LiveData<Resource<Character>> = repository.getCharacter()
    val episodes: LiveData<Resource<List<Episode>>> = repository.getEpisodes()

    fun getCharacter(url: String) {
        repository.fetchCharacter(url)
    }

    fun getEpisodes(urls: List<String>) {
        repository.fetchEpisodes(urls)
    }

}