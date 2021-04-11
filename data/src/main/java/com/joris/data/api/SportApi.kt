package com.joris.data.api

import retrofit2.http.GET
import retrofit2.http.Query

internal interface SportApi {

    companion object {
        const val API_BASE_URL = "https://www.thesportsdb.com/api/v1/json/1/"
        const val CONNECT_TIMEOUT_SECONDS: Long = 30
        const val READ_TIMEOUT_SECONDS: Long = 30
    }

    //region Teams
    @GET("search_all_teams.php")
    suspend fun getTeamsFromLeague(
        @Query("l") league: String
    ): TeamListResponse?

    @GET("searchteams.php")
    suspend fun getTeamDetails(
        @Query("t") teamName: String
    ): TeamListResponse?
    //endregion
}