package com.iliazusik.rickmortyapp.di

import com.ilia_zusik.rickmortyapp.BuildConfig
import com.iliazusik.rickmortyapp.network.CharacterApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Module {

    @Singleton
    @Provides
    fun provideCharacterApi(): CharacterApi {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASEURL)
            .client(
                OkHttpClient.Builder()
                    .addInterceptor(HttpLoggingInterceptor().apply {
                        setLevel(HttpLoggingInterceptor.Level.BODY)
                    })
                    .connectTimeout(5L, TimeUnit.SECONDS)
                    .readTimeout(5L, TimeUnit.SECONDS)
                    .writeTimeout(5L, TimeUnit.SECONDS)
                    .build()
            )
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(CharacterApi::class.java)
    }

}