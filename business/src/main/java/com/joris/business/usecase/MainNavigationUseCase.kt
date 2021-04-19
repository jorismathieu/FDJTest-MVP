package com.joris.business.usecase

import com.joris.business.entity.Screen
import com.joris.business.usecase.base.ObservableUseCase
import java.lang.ref.WeakReference

interface MainNavigationUseCase : ObservableUseCase<MainNavigationUseCase.ScreenChangeObserver> {
    interface ScreenChangeObserver {
        fun onScreenChange(screen: Screen, params: Map<String, String?>? = null)
        fun onGoToPreviousScreen()
    }

    fun switchToTeamDetail(params: Map<String, String?>? = null)
    fun onPressBack()
}

class MainNavigationUseCaseImpl : MainNavigationUseCase {
    private var screenChangeObserverWeakRef: WeakReference<MainNavigationUseCase.ScreenChangeObserver>? = null

    override fun switchToTeamDetail(params: Map<String, String?>?) {
        screenChangeObserverWeakRef?.get()?.onScreenChange(Screen.DETAIL, params)
    }

    override fun onPressBack() {
        screenChangeObserverWeakRef?.get()?.onGoToPreviousScreen()
    }

    override fun subscribe(observer: MainNavigationUseCase.ScreenChangeObserver) {
        screenChangeObserverWeakRef = WeakReference(observer)
    }

    override fun unsubscribe() {
        screenChangeObserverWeakRef = null
    }

}