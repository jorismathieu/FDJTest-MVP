package com.joris.business.usecase.base


interface ObservableUseCase<Observer> {
    fun subscribe(observer: Observer)
    fun unsubscribe()
}
