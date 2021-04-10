package com.joris.data.datasource

import com.google.gson.GsonBuilder
import com.joris.business.entity.Team
import com.joris.data.api.SportApi
import com.joris.data.api.SportApi.Companion.CONNECT_TIMEOUT_SECONDS
import com.joris.data.api.SportApi.Companion.READ_TIMEOUT_SECONDS
import com.joris.data.datasource.base.TeamRemoteDataSource
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


class TeamRemoteDataSourceImpl : TeamRemoteDataSource {

    private val sportApi: SportApi by lazy {
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .client(
                OkHttpClient.Builder()
                    .connectTimeout(CONNECT_TIMEOUT_SECONDS, TimeUnit.SECONDS)
                    .readTimeout(READ_TIMEOUT_SECONDS, TimeUnit.SECONDS)
                    .build()
            )
            .baseUrl(SportApi.API_BASE_URL)
            .build()
            .create(SportApi::class.java)
    }

    override suspend fun getTeamsFromLeague(league: String): List<Team>? {
        return sportApi.getTeamsFromLeague(String(league.toByteArray(), Charsets.UTF_8))?.teams
    }
}