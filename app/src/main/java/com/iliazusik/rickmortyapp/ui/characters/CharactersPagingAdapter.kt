package com.iliazusik.rickmortyapp.ui.characters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.liveData
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.ilia_zusik.rickmortyapp.databinding.ItemCharacterBinding
import com.iliazusik.rickmortyapp.data.Character
import com.iliazusik.rickmortyapp.data.network.CharacterApi
import com.iliazusik.rickmortyapp.ui.UiHelper
import kotlinx.coroutines.Dispatchers

class CharactersPagingAdapter(
    private val api: CharacterApi
) : PagingDataAdapter<Character, CharactersPagingAdapter.CharacterViewHolder>(DIFF_UTIL_CALL_BACK) {

    private companion object {
        val DIFF_UTIL_CALL_BACK: DiffUtil.ItemCallback<Character> =
            object : DiffUtil.ItemCallback<Character>() {
                override fun areItemsTheSame(oldItem: Character, newItem: Character): Boolean {
                    return oldItem.url == newItem.url
                }

                override fun areContentsTheSame(oldItem: Character, newItem: Character): Boolean {
                    return oldItem == newItem
                }
            }
    }

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        getItem(position)?.let { holder.onBind(it) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        return CharacterViewHolder(
            ItemCharacterBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    fun setOnItemClickListener(onClick: (String) -> Unit) {
        onItemClick = onClick
    }

    private var onItemClick: ((String) -> Unit)? = null

    inner class CharacterViewHolder(private val binding: ItemCharacterBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun onBind(character: Character) {
            binding.root.setOnClickListener {
                onItemClick?.let {
                    it(character.url)
                }
            }
            binding.apply {
                tvCharacterName.text = character.name

                if (character.firsSeenEpisodeName.isNullOrEmpty()) {
                    liveData<String>(Dispatchers.IO) {
                        try {
                            character.episode.getOrNull(0)?.apply {
                                api.getSingleEpisode(this).let { episodeResponse ->
                                    if (episodeResponse.isSuccessful && episodeResponse.body() != null && episodeResponse.code() in 200..300) {
                                        episodeResponse.body()?.let { episode ->
                                            character.firsSeenEpisodeName = episode.name
                                            tvFirstSeen.text = episode.name
                                        }
                                    }
                                }
                            }
                        } catch (_: Exception) {

                        }
                    }.apply {
                        this.value?.let {

                        }
                    }
                } else {
                    tvFirstSeen.text = character.firsSeenEpisodeName
                }

                tvLastLocation.text = character.location.name
                "${character.status} - ${character.gender}".apply {
                    tvCharacterStatus.text = this
                }
                ivAvatar.load(character.image)
                UiHelper.setStatusDot(character.status, tvCharacterStatus)
            }
        }
    }
}
