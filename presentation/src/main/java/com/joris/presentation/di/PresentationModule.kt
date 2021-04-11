package com.joris.presentation.di

import com.joris.business.gateway.LogguerGateway
import com.joris.business.usecase.*
import com.joris.presentation.gateway.LogguerGatewayImpl
import com.joris.presentation.presenter.*
import org.koin.dsl.module

val presentationModule = module {

    // Gateways
    single<LogguerGateway> {LogguerGatewayImpl()}

    // Use cases
    single<MainNavigationUseCase> { MainNavigationUseCaseImpl() }
    factory<GetTeamsFromLeagueUseCase> { GetTeamsFromLeagueUseCaseImpl(get(), get()) }
    factory<GetTeamDetailsUseCase> { GetTeamDetailsUseCaseImpl(get(), get()) }

    // Presenters
    factory<MainNavigationPresenter> { params -> MainNavigationPresenterImpl(params[0], get()) }
    factory<SearchPresenter> { params -> SearchPresenterImpl(params[0], get(), get()) }
    factory<DetailsPresenter> { params -> DetailsPresenterImpl(params[0], get()) }
}
