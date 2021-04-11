package com.joris.business.usecase.base

enum class CriticalErrorCode{
    DATA_ACCESS,
    INTERNAL
}

abstract class SuspendUseCaseOutput<Type> {
    abstract val criticalError: CriticalErrorCode?
    abstract val data: Type

    fun containsCriticalError() = criticalError != null
}