package com.iliazusik.rickmortyapp.di

import com.ilia_zusik.rickmortyapp.BuildConfig
import com.iliazusik.rickmortyapp.data.network.CharacterApi
import com.iliazusik.rickmortyapp.data.repository.CharacterRepository
import com.iliazusik.rickmortyapp.data.repository.CharactersRepository
import com.iliazusik.rickmortyapp.ui.character.CharacterViewModel
import com.iliazusik.rickmortyapp.ui.character.EpisodesRecyclerViewAdapter
import com.iliazusik.rickmortyapp.ui.characters.CharactersRecyclerViewAdapter
import com.iliazusik.rickmortyapp.ui.characters.CharactersViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


val networkModule = module {
    single { provideLoggingInterceptor() }
    single { provideOkHttpClient(get()) }
    single { provideRetrofit(get()) }
    single { provideApi(get()) }
}

val repositoryModule = module {
    factory { provideCharacterRepository(get()) }
    factory { provideCharactersRepository(get()) }
}

val viewModelModule = module {
    viewModel<CharacterViewModel> { provideCharacterViewModel(get()) }
    viewModel<CharactersViewModel> { provideCharactersViewModel(get()) }
}

val recyclerViewAdapterModule = module {
    factory { provideCharacterAdapter(get()) }
    factory { provideEpisodesAdapter() }
}

val modules =
    listOf(networkModule, repositoryModule, viewModelModule, recyclerViewAdapterModule)

fun provideCharacterViewModel(repository: CharacterRepository) =
    CharacterViewModel(repository)

fun provideCharactersViewModel(repository: CharactersRepository) =
    CharactersViewModel(repository)

fun provideCharacterRepository(api: CharacterApi) =
    CharacterRepository(api)

fun provideCharactersRepository(api: CharacterApi) =
    CharactersRepository(api)

fun provideEpisodesAdapter() =
    EpisodesRecyclerViewAdapter()

fun provideCharacterAdapter(api: CharacterApi) =
    CharactersRecyclerViewAdapter(api)

fun provideApi(retrofit: Retrofit): CharacterApi =
    retrofit.create(CharacterApi::class.java)

fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit =
    Retrofit.Builder()
        .baseUrl(BuildConfig.BASEURL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

fun provideOkHttpClient(interceptor: HttpLoggingInterceptor): OkHttpClient =
    OkHttpClient.Builder()
        .addInterceptor(interceptor)
        .connectTimeout(5L, TimeUnit.SECONDS)
        .readTimeout(5L, TimeUnit.SECONDS)
        .writeTimeout(5L, TimeUnit.SECONDS)
        .build()

fun provideLoggingInterceptor(): HttpLoggingInterceptor =
    HttpLoggingInterceptor().apply {
        setLevel(HttpLoggingInterceptor.Level.BODY)
    }