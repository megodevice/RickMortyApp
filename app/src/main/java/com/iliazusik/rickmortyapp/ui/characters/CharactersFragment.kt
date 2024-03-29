package com.iliazusik.rickmortyapp.ui.characters

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.ilia_zusik.rickmortyapp.databinding.FragmentCharactersBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


class CharactersFragment : Fragment() {

    private val viewModel: CharactersViewModel by viewModel()

    private val charactersPagingAdapter: CharactersPagingAdapter by inject()

    private var _binding: FragmentCharactersBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCharactersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setCharactersRV()
        observe()
    }

    private fun setCharactersRV() = with(binding.rvCharacters) {
        adapter = charactersPagingAdapter
        layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL,
            false
        )
    }


    private fun observe() {

        charactersPagingAdapter.getOnItemClickUrl().observe(viewLifecycleOwner) {
            findNavController().navigate(
                CharactersFragmentDirections.actionCharactersFragmentToCharacterFragment(
                    it
                )
            )
        }

        viewModel.characterList.observe(viewLifecycleOwner) {

            charactersPagingAdapter.submitData(viewLifecycleOwner.lifecycle, it)
        }

        lifecycleScope.launch {
            charactersPagingAdapter.loadStateFlow.collectLatest {
                binding.animLoading.isVisible =
                    (it.refresh is LoadState.Loading) || (it.append is LoadState.Loading)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}