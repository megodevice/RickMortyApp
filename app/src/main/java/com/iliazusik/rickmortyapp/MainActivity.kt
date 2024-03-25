package com.iliazusik.rickmortyapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.ilia_zusik.rickmortyapp.databinding.ActivityMainBinding
import com.iliazusik.rickmortyapp.ui.CharacterViewModel
import com.iliazusik.rickmortyapp.ui.CharactersRecyclerViewAdapter
import com.iliazusik.rickmortyapp.utils.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: CharacterViewModel by viewModels()
    private val charactersAdapter: CharactersRecyclerViewAdapter by lazy {
        CharactersRecyclerViewAdapter()
    }

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setCharactersRV()
        observe()
        viewModel.getCharacters()
    }

    private fun setCharactersRV() = with(binding.rvCharacters) {
        adapter = charactersAdapter
        layoutManager = LinearLayoutManager(
            this@MainActivity,
            LinearLayoutManager.VERTICAL,
            false
        )
    }


    private fun observe() {
        viewModel.characters.observe(this) {
            when (it) {
                is Resource.Error -> {
                    Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                }

                is Resource.Loading -> {

                }

                is Resource.Success -> {
                    charactersAdapter.submitList(it.data)
                }
            }
        }
    }
}