package com.joris.data.repository

import com.joris.business.entity.Team
import com.joris.business.repository.TeamRepository
import com.joris.data.datasource.base.TeamRemoteDataSource

// We could add another dataSource if needed, a local one (eg Room), to add some object cache
class TeamRepositoryImpl(private val teamRemoteDataSource: TeamRemoteDataSource): TeamRepository {
    override suspend fun getTeamsFromLeague(league: String): List<Team>? {
        return teamRemoteDataSource.getTeamsFromLeague(league)
    }

    override suspend fun getTeamDetails(teamName: String): Team? {
        // Instead of a remote request, we could use some local cache to get team information
        return teamRemoteDataSource.getTeamDetails(teamName)
    }
}