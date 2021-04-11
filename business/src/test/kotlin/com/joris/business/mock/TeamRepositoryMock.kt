package com.joris.business.mock

import com.joris.business.entity.Team
import com.joris.business.repository.TeamRepository

fun getTeamRepositoryMock(
    getTeamsFromLeagueLambda: () -> List<Team>? = { throw Exception() },
    getTeamDetailLambda: () -> Team? = { throw Exception() },
) = object : TeamRepository {
    override suspend fun getTeamsFromLeague(league: String): List<Team>? {
        return getTeamsFromLeagueLambda()
    }

    override suspend fun getTeamDetails(teamName: String): Team? {
        return getTeamDetailLambda()
    }
}