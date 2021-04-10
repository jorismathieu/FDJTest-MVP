package com.joris.business.usecase.base

interface SuspendUseCase<InputType, OutputType> {
    suspend fun execute(input: InputType): OutputType
}