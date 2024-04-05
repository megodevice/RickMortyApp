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
import com.ilia_zusik.rickmortyapp.databinding.FragmentCharacterBinding
import com.iliazusik.rickmortyapp.data.models.Character
import com.iliazusik.rickmortyapp.data.models.Episodes
import com.iliazusik.rickmortyapp.ui.UiHelper
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
        viewModel.getCharacter(args.characterUrl).observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Error -> {
                    Toast.makeText(requireContext(), resource.message, Toast.LENGTH_LONG).show()
                }

                is Resource.Loading -> {}

                is Resource.Success -> {
                    resource.data?.let { character ->
                        setBasicCharacterInfo(character)
                        if (character.episode.size > 1) {
                            viewModel.getEpisodes(character.episode)
                                .observe(viewLifecycleOwner) { episodes ->
                                    when (episodes) {
                                        is Resource.Error -> {
                                            Toast.makeText(
                                                requireContext(),
                                                episodes.message,
                                                Toast.LENGTH_LONG
                                            ).show()
                                        }

                                        is Resource.Loading -> {}

                                        is Resource.Success -> {
                                            episodes.data?.let { episodesList ->
                                                binding.tvFirstSeen.text = episodesList[0].name
                                                episodesAdapter.submitList(episodesList)
                                            }
                                        }
                                    }
                                    binding.animLoadingEpisodes.isVisible =
                                        episodes is Resource.Loading
                                }
                        } else {
                            viewModel.getEpisode(character.episode[0])
                                .observe(viewLifecycleOwner) { episodes ->
                                    when (episodes) {
                                        is Resource.Error -> {
                                            Toast.makeText(
                                                requireContext(),
                                                episodes.message,
                                                Toast.LENGTH_LONG
                                            ).show()
                                        }

                                        is Resource.Loading -> {}

                                        is Resource.Success -> {
                                            episodes.data?.let { episode ->
                                                binding.tvFirstSeen.text = episode.name
                                                episodesAdapter.submitList(Episodes().apply { add(episode) })
                                            }
                                        }
                                    }
                                    binding.animLoadingEpisodes.isVisible =
                                        episodes is Resource.Loading
                                }
                        }
                    }
                }
            }
            binding.animLoading.isVisible = resource is Resource.Loading
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
            UiHelper.setStatusDot(character.status, tvCharacterStatus)
        }
    }
}