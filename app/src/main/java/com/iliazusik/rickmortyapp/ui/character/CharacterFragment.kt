package com.iliazusik.rickmortyapp.ui.character

import androidx.core.view.isVisible
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.ilia_zusik.rickmortyapp.databinding.FragmentCharacterBinding
import com.iliazusik.rickmortyapp.data.models.Character
import com.iliazusik.rickmortyapp.data.models.Episode
import com.iliazusik.rickmortyapp.data.models.Episodes
import com.iliazusik.rickmortyapp.ui.base.BaseFragment
import com.iliazusik.rickmortyapp.ui.UiHelper
import org.koin.androidx.viewmodel.ext.android.viewModel


class CharacterFragment : BaseFragment<FragmentCharacterBinding, CharacterViewModel>(
    FragmentCharacterBinding::inflate
) {
    override val viewModel: CharacterViewModel by viewModel()

    private val args: CharacterFragmentArgs by navArgs()

    private val episodesAdapter: EpisodesRecyclerViewAdapter by lazy {
        EpisodesRecyclerViewAdapter()
    }

    private fun setEpisodesRV() = with(binding.rvEpisodes) {
        adapter = episodesAdapter
        layoutManager = LinearLayoutManager(
            requireContext(), LinearLayoutManager.VERTICAL, false
        )
    }

    override fun initialize() {
        viewModel.getCharacter(args.characterUrl)
        setEpisodesRV()
    }

    private fun setBasicLoading(loading: Boolean) {
        binding.animLoading.isVisible = loading
    }

    private fun setEpisodesLoading(loading: Boolean) {
        binding.animLoadingEpisodes.isVisible = loading
    }

    private fun <T> submitEpisodes(episode: T) {
        if (episode is Episodes) {
            episodesAdapter.submitList(episode)
            binding.tvFirstSeen.text = episode.getOrNull(0)?.name
        }
        if (episode is Episode) {
            episodesAdapter.submitList(Episodes().apply { add(episode) })
            binding.tvFirstSeen.text = episode.name
        }
    }

    override fun observe() {
        viewModel.getCharacter(args.characterUrl).resHandler(
            this::setBasicLoading,
            this::setBasicCharacterInfo
        )
    }

    private fun setBasicCharacterInfo(character: Character) {
        binding.apply {
            tvCharacterName.text = character.name
            tvLastLocation.text = character.location.name
            "${character.status} - ${character.gender}".apply {
                tvCharacterStatus.text = this
            }
            ivAvatar.load(character.image)
            UiHelper.setStatusDot(character.status, tvCharacterStatus)
        }
        if (character.episodes.size > 1) {
            viewModel.getEpisodes(character.episodes).resHandler(
                this::setEpisodesLoading,
                this::submitEpisodes
            )
        } else {
            viewModel.getEpisode(character.episodes[0]).resHandler(
                this::setEpisodesLoading,
                this::submitEpisodes
            )
        }
    }
}