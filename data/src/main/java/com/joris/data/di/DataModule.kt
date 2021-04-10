package com.joris.data.di

import com.joris.business.repository.TeamRepository
import com.joris.data.datasource.TeamRemoteDataSourceImpl
import com.joris.data.datasource.base.TeamRemoteDataSource
import com.joris.data.repository.TeamRepositoryImpl
import org.koin.dsl.module

val dataModule = module {

    // Data sources
    single<TeamRemoteDataSource> { TeamRemoteDataSourceImpl() }

    // Repositories
    single<TeamRepository> { TeamRepositoryImpl(get()) }
}


