package com.joris.data.datasource.base

import com.joris.business.entity.Team


interface TeamRemoteDataSource {
    suspend fun getTeamsFromLeague(league: String): List<Team>?
    suspend fun getTeamDetails(league: String): Team?
}