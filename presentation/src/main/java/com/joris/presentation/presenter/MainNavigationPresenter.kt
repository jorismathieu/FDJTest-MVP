package com.joris.presentation.presenter

import android.os.Bundle
import com.joris.business.entity.Screen
import com.joris.business.usecase.MainNavigationUseCase

interface MainNavigationPresenter {
    interface View {
        fun goToPreviousScreen()
        fun switchToScreen(screen: Screen, args: Bundle? = null)
    }

    fun onBackPressed()

    fun subscribeToScreenChanges()
    fun unsubscribeToScreenChanges()
}

class MainNavigationPresenterImpl(
    private val view: MainNavigationPresenter.View,
    private val mainNavigationUseCase: MainNavigationUseCase,
) : MainNavigationPresenter {

    private val screenChangeObserver: MainNavigationUseCase.ScreenChangeObserver by lazy {
        object : MainNavigationUseCase.ScreenChangeObserver {
            override fun switchToPreviousScreen() {
                view.goToPreviousScreen()
            }

            override fun onScreenChange(screen: Screen, params: Map<String, String?>?) {
                val args = params?.let { map ->
                    Bundle().apply {
                        for (key in map.keys) {
                            putString(key, map[key])
                        }
                    }
                }
                view.switchToScreen(screen, args)
            }

        }
    }

    override fun onBackPressed() {
        mainNavigationUseCase.switchToPreviousScreen()
    }

    override fun subscribeToScreenChanges() {
        mainNavigationUseCase.subscribe(screenChangeObserver)
    }

    override fun unsubscribeToScreenChanges() {
        mainNavigationUseCase.unsubscribe()
    }

}