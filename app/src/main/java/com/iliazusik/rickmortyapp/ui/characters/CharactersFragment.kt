package com.iliazusik.rickmortyapp.ui.characters

import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.ilia_zusik.rickmortyapp.databinding.FragmentCharactersBinding
import com.iliazusik.rickmortyapp.ui.base.BaseFragment
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


class CharactersFragment :
    BaseFragment<FragmentCharactersBinding, CharactersViewModel>(FragmentCharactersBinding::inflate) {

    override val viewModel: CharactersViewModel by viewModel()

    private val charactersPagingAdapter: CharactersPagingAdapter by inject()

    private fun setCharactersRecyclerView() = with(binding.rvCharacters) {
        adapter = charactersPagingAdapter
        layoutManager = LinearLayoutManager(
            requireContext(), LinearLayoutManager.VERTICAL, false
        )
        charactersPagingAdapter.setOnItemClickListener {
            findNavController().navigate(
                CharactersFragmentDirections.actionCharactersFragmentToCharacterFragment(
                    it
                )
            )
        }
    }

    override fun initialize() {
        setCharactersRecyclerView()
    }

    override fun observe() {

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
}