package com.iliazusik.rickmortyapp.data.repository

import com.iliazusik.rickmortyapp.data.network.CharacterApi

class CharacterRepository(
    private val api: CharacterApi
) : BaseRepository() {
    fun fetchCharacter(url: String) = doRequest { api.getCharacter(url) }

    fun fetchEpisodes(urls: List<String>) = doRequest { api.getEpisodes(convertUrls(urls)) }

    fun fetchEpisode(url: String) = doRequest { api.getEpisode(url) }

}