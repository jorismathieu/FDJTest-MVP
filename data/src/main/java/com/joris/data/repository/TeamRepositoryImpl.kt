package com.joris.data.repository

import com.joris.business.entity.Team
import com.joris.business.repository.TeamRepository
import com.joris.data.datasource.base.TeamRemoteDataSource

// We could add another dataSource if needed, a local one (eg Room), to add some object cache
class TeamRepositoryImpl(private val teamRemoteDataSource: TeamRemoteDataSource): TeamRepository {
    override suspend fun getTeamsFromLeague(league: String): List<Team>? {
        return teamRemoteDataSource.getTeamsFromLeague(league)
    }
}