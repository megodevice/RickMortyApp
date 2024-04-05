package com.iliazusik.rickmortyapp.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.iliazusik.rickmortyapp.data.models.Character
import com.iliazusik.rickmortyapp.data.network.CharacterApi

class CharactersPagingSource(
    private val api: CharacterApi
) : PagingSource<Int, Character>() {
    override fun getRefreshKey(state: PagingState<Int, Character>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Character> {
        return try {
            val position = params.key ?: 1
            val response = api.getCharactersByPage(page = position.toString())

            if (response.isSuccessful && response.body() != null) {
                LoadResult.Page(
                    data = response.body()!!.characters,
                    prevKey = if (position == 1) null else (position - 1),
                    nextKey = if (position == response.body()?.requestInfo?.pages) null else (position + 1)
                )
            } else {
                @Suppress("UNREACHABLE_CODE")
                LoadResult.Error(throw Exception("No Response"))
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}

