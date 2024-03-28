package com.iliazusik.rickmortyapp.ui.characters

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ilia_zusik.rickmortyapp.databinding.FragmentCharactersBinding
import com.iliazusik.rickmortyapp.utils.Resource
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


class CharactersFragment : Fragment() {

    private val viewModel: CharactersViewModel by viewModel()

    private val charactersAdapter: CharactersRecyclerViewAdapter by inject()

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
        viewModel.getCharacters()
    }

    private fun setCharactersRV() = with(binding.rvCharacters) {
        adapter = charactersAdapter
        layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL,
            false
        )
    }


    private fun observe() {

        charactersAdapter.getOnItemClickUrl().observe(viewLifecycleOwner) {
            findNavController().navigate(
                CharactersFragmentDirections.actionCharactersFragmentToCharacterFragment(
                    it
                )
            )
        }

        viewModel.characters.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Error -> {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                }

                is Resource.Loading -> {
                    binding.animLoading.visibility = View.VISIBLE
                }

                is Resource.Success -> {
                    charactersAdapter.submitList(it.data)
                }
            }
            if (it !is Resource.Loading) {
                binding.animLoading.visibility = View.GONE
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}