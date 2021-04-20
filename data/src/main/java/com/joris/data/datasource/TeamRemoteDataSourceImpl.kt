package com.joris.data.datasource

import com.google.gson.GsonBuilder
import com.joris.business.entity.Team
import com.joris.data.api.ApiConfiguration
import com.joris.data.api.SportApi
import com.joris.data.datasource.base.TeamRemoteDataSource
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


class TeamRemoteDataSourceImpl(apiConfiguration: ApiConfiguration) : TeamRemoteDataSource {

    private val sportApi: SportApi by lazy {
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .client(
                OkHttpClient.Builder()
                    .connectTimeout(apiConfiguration.CONNECT_TIMEOUT_SECONDS, TimeUnit.SECONDS)
                    .readTimeout(apiConfiguration.READ_TIMEOUT_SECONDS, TimeUnit.SECONDS)
                    .build()
            )
            .baseUrl(apiConfiguration.API_BASE_URL)
            .build()
            .create(SportApi::class.java)
    }

    @Throws(Throwable::class)
    override suspend fun getTeamsFromLeague(league: String): List<Team>? {
        return sportApi.getTeamsFromLeague(
            league = String(
                league.toByteArray(),
                Charsets.UTF_8
            )
        )?.teams
    }

    @Throws(Throwable::class)
    override suspend fun getTeamDetails(league: String): Team? {
        return sportApi.getTeamDetails(
            teamName = String(
                league.toByteArray(),
                Charsets.UTF_8
            )
        )?.teams?.get(0)
    }
}