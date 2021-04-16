package com.joris.fdj.di

import com.joris.business.gateway.LogguerGateway
import com.joris.business.repository.TeamRepository
import com.joris.business.usecase.*
import com.joris.data.datasource.TeamRemoteDataSourceImpl
import com.joris.data.datasource.base.TeamRemoteDataSource
import com.joris.data.repository.TeamRepositoryImpl
import com.joris.presentation.gateway.LogguerGatewayImpl
import com.joris.presentation.presenter.*
import org.koin.dsl.module


// Koin module can be replaced with Hilt (when a stable release is available) or Dagger
val appModule = module {

    // Business
    // Use cases
    single<MainNavigationUseCase> { MainNavigationUseCaseImpl() }
    factory<GetTeamsFromLeagueUseCase> { GetTeamsFromLeagueUseCaseImpl(get(), get()) }
    factory<GetTeamDetailsUseCase> { GetTeamDetailsUseCaseImpl(get(), get()) }

    // Data
    // Repositories
    single<TeamRepository> { TeamRepositoryImpl(get()) }
    // Sources
    single<TeamRemoteDataSource> { TeamRemoteDataSourceImpl() }

    // Presentation
    // Gateways
    single<LogguerGateway> { LogguerGatewayImpl() }
    // Presenters
    factory<MainNavigationPresenter> { params -> MainNavigationPresenterImpl(params[0], get()) }
    factory<SearchPresenter> { params -> SearchPresenterImpl(params[0], get(), get()) }
    factory<DetailsPresenter> { params -> DetailsPresenterImpl(params[0], get()) }

}