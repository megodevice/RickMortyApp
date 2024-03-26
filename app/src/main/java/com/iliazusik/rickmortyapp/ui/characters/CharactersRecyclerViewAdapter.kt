package com.iliazusik.rickmortyapp.ui.characters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.ilia_zusik.rickmortyapp.BuildConfig
import com.ilia_zusik.rickmortyapp.R
import com.ilia_zusik.rickmortyapp.databinding.ItemCharacterBinding
import com.iliazusik.rickmortyapp.data.Character
import com.iliazusik.rickmortyapp.data.EpisodeModel
import com.iliazusik.rickmortyapp.data.network.CharacterApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject


class CharactersRecyclerViewAdapter @Inject constructor(private val api: CharacterApi) :
    ListAdapter<Character, CharactersRecyclerViewAdapter.CharacterViewHolder>(DIFF_UTIL_CALL_BACK) {


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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        return CharacterViewHolder(
            ItemCharacterBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        holder.onBind(getItem(position))
    }


    inner class CharacterViewHolder(private val binding: ItemCharacterBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun onBind(character: Character) {
            binding.apply {
                tvCharacterName.text = character.name

                if (character.firsSeenEpisodeName.isNullOrEmpty()) {
                    character.episode.getOrNull(0)?.apply {
                        api.getSingleEpisode(this).enqueue(object : Callback<EpisodeModel> {
                            override fun onResponse(
                                episodeCall: Call<EpisodeModel>,
                                episodeResponse: Response<EpisodeModel>
                            ) {
                                if (episodeResponse.isSuccessful && episodeResponse.body() != null && episodeResponse.code() in 200..300) {
                                    episodeResponse.body()?.let { episodeModel ->
                                        character.firsSeenEpisodeName = episodeModel.name
                                        tvFirstSeen.text = episodeModel.name
                                    }
                                }
                            }

                            override fun onFailure(p0: Call<EpisodeModel>, p1: Throwable) {}
                        })
                    }
                } else {
                    tvFirstSeen.text = character.firsSeenEpisodeName
                }

                tvLastLocation.text = character.location.name
                "${character.status} - ${character.gender}".apply {
                    tvCharacterStatus.text = this
                }
                ivAvatar.load(character.image)
                when (character.status) {
                    BuildConfig.ALIVE -> {
                        tvCharacterStatus.setCompoundDrawablesWithIntrinsicBounds(
                            R.drawable.ic_dot_alive, 0, 0, 0
                        )
                    }

                    BuildConfig.DEATH -> {
                        tvCharacterStatus.setCompoundDrawablesWithIntrinsicBounds(
                            R.drawable.ic_dot_death, 0, 0, 0
                        )
                    }

                    else -> {
                        tvCharacterStatus.setCompoundDrawablesWithIntrinsicBounds(
                            R.drawable.ic_dot_unknown, 0, 0, 0
                        )
                    }
                }
            }
        }
    }
}


