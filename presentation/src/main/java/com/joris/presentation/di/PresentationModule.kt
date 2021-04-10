package com.joris.presentation.di

import com.joris.business.gateway.LogguerGateway
import com.joris.business.usecase.GetTeamsFromLeagueUseCase
import com.joris.business.usecase.GetTeamsFromLeagueUseCaseImpl
import com.joris.presentation.gateways.LogguerGatewayImpl
import org.koin.dsl.module

val presentationModule = module {

    // Gateways
    single<LogguerGateway> {LogguerGatewayImpl()}

    // Use cases
    factory<GetTeamsFromLeagueUseCase> { GetTeamsFromLeagueUseCaseImpl(get(), get()) }
}
