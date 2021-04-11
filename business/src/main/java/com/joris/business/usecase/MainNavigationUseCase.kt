package com.joris.business.usecase

import com.joris.business.entity.Screen
import com.joris.business.usecase.base.ObservableUseCase
import java.lang.ref.WeakReference

interface MainNavigationUseCase : ObservableUseCase<MainNavigationUseCase.ScreenChangeObserver> {
    interface ScreenChangeObserver {
        fun switchToPreviousScreen()
        fun onScreenChange(screen: Screen, params: Map<String, String?>? = null)
    }

    fun switchToPreviousScreen()
    fun switchToScreen(screen: Screen, params: Map<String, String?>? = null)
}

class MainNavigationUseCaseImpl : MainNavigationUseCase {
    private var screenChangeObserverWeakRef: WeakReference<MainNavigationUseCase.ScreenChangeObserver>? = null

    override fun switchToPreviousScreen() {
        screenChangeObserverWeakRef?.get()?.switchToPreviousScreen()
    }

    override fun switchToScreen(screen: Screen, params: Map<String, String?>?) {
        screenChangeObserverWeakRef?.get()?.onScreenChange(screen, params)
    }

    override fun subscribe(observer: MainNavigationUseCase.ScreenChangeObserver) {
        screenChangeObserverWeakRef = WeakReference(observer)
    }

    override fun unsubscribe() {
        screenChangeObserverWeakRef = null
    }

}