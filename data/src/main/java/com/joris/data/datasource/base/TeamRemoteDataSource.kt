package com.joris.data.datasource.base

import com.joris.business.entity.Team


interface TeamRemoteDataSource {
    @Throws(Throwable::class)
    suspend fun getTeamsFromLeague(league: String): List<Team>?
}