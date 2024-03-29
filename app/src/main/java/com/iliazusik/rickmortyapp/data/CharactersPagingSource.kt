package com.iliazusik.rickmortyapp.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.iliazusik.rickmortyapp.utils.Resource

class CharactersPagingSource : PagingSource<Int, Resource<List<Character>>>() {
    override fun getRefreshKey(state: PagingState<Int, Resource<List<Character>>>): Int? {
        TODO("Not yet implemented")
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Resource<List<Character>>> {
        TODO("Not yet implemented")
    }
}