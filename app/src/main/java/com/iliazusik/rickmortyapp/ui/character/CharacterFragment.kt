package com.iliazusik.rickmortyapp.ui.character

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.ilia_zusik.rickmortyapp.BuildConfig
import com.ilia_zusik.rickmortyapp.R
import com.ilia_zusik.rickmortyapp.databinding.FragmentCharacterBinding
import com.iliazusik.rickmortyapp.data.Character
import com.iliazusik.rickmortyapp.utils.Resource
import org.koin.androidx.viewmodel.ext.android.viewModel


class CharacterFragment : Fragment() {

    private var _binding: FragmentCharacterBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CharacterViewModel by viewModel()

    private val args: CharacterFragmentArgs by navArgs()

    private val episodesAdapter: EpisodesRecyclerViewAdapter by lazy {
        EpisodesRecyclerViewAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCharacterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getCharacter(args.characterUrl)
        setEpisodesRV()
        observe()
    }

    private fun setEpisodesRV() = with(binding.rvEpisodes) {
        adapter = episodesAdapter
        layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL,
            false
        )
    }

    private fun observe() {
        viewModel.character.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Error -> {
                    Toast.makeText(requireContext(), resource.message, Toast.LENGTH_LONG).show()
                }

                is Resource.Loading -> {}

                is Resource.Success -> {
                    resource.data?.let {
                        setBasicCharacterInfo(it)
                        viewModel.getEpisodes(it.episode)
                    }
                }
            }
            binding.animLoading.isVisible = resource is Resource.Loading
        }

        viewModel.episodes.observe(viewLifecycleOwner) { episodes ->
            when (episodes) {
                is Resource.Error -> {
                    Toast.makeText(requireContext(), episodes.message, Toast.LENGTH_LONG).show()
                }

                is Resource.Loading -> {}

                is Resource.Success -> {
                    episodes.data?.let {
                        binding.tvFirstSeen.text = it[0].name
                        episodesAdapter.submitList(it)
                    }
                }
            }
            binding.animLoadingEpisodes.isVisible = episodes is Resource.Loading
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun setBasicCharacterInfo(character: Character) {
        binding.apply {
            tvCharacterName.text = character.name
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