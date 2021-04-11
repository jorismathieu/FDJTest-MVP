package com.joris.business.repository

import com.joris.business.entity.Team

// Abstract definition of the repository we need to get Teams information
interface TeamRepository {
    @Throws(Throwable::class)
    suspend fun getTeamsFromLeague(league: String): List<Team>?

    @Throws(Throwable::class)
    suspend fun getTeamDetails(teamName: String): Team?
}