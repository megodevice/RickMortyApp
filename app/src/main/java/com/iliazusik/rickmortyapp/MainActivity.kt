package com.iliazusik.rickmortyapp

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.ilia_zusik.rickmortyapp.databinding.ActivityMainBinding
import com.iliazusik.rickmortyapp.ui.CharacterViewModel
import com.iliazusik.rickmortyapp.ui.CharactersRecyclerViewAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: CharacterViewModel by viewModels()

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initView()
        initListeners()
        observe()

        viewModel.giveCharacters()
    }

    private fun initView() {
        binding.rvCharacters.adapter = CharactersRecyclerViewAdapter()
    }

    private fun observe() {
        viewModel.characters.observe(this) {
            (binding.rvCharacters.adapter as CharactersRecyclerViewAdapter).submitList(it)
        }
    }

    private fun initListeners() {
        binding.root.setOnClickListener {

        }
    }
}