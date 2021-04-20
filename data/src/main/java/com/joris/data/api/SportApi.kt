package com.joris.data.api

import com.google.gson.annotations.SerializedName
import com.joris.data.entity.TeamImpl
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface ApiConfiguration {
    val API_BASE_URL: String
    val CONNECT_TIMEOUT_SECONDS: Long
    val READ_TIMEOUT_SECONDS: Long
    val HEADER_DEFAULT_CACHE_MAX_AGE: String
}

/**
 * Definition of the API we consume
 */
interface SportApi {

    companion object : ApiConfiguration {
        override val API_BASE_URL = "https://www.thesportsdb.com/api/v1/json/1/"
        override val CONNECT_TIMEOUT_SECONDS: Long = 30
        override val READ_TIMEOUT_SECONDS: Long = 30
        override val HEADER_DEFAULT_CACHE_MAX_AGE = "Cache-Control: max-age=300" // Cache is 5min, we do not need a fresher data here
    }

    //region Teams
    @GET("search_all_teams.php")
    suspend fun getTeamsFromLeague(
        @Header("Cache-Control") cacheControl: String = HEADER_DEFAULT_CACHE_MAX_AGE,
        @Query("l") league: String
    ): TeamListResponse?

    @GET("searchteams.php")
    suspend fun getTeamDetails(
        @Header("Cache-Control") cacheControl: String = HEADER_DEFAULT_CACHE_MAX_AGE,
        @Query("t") teamName: String
    ): TeamListResponse?
    //endregion
}

class TeamListResponse {
    @SerializedName("teams")
    var teams: List<TeamImpl>? = null
}