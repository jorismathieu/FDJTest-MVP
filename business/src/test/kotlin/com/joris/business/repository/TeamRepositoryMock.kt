package com.joris.business.repository

import com.joris.business.entity.Team

fun getTeamRepositoryMock(
    getTeamsFromLeagueLambda: () -> List<Team>?,
) = object : TeamRepository {
    override suspend fun getTeamsFromLeague(league: String): List<Team>? {
        return getTeamsFromLeagueLambda()
    }
}