package com.joris.fdj

import android.app.Application
import com.joris.fdj.di.appModule
import org.koin.core.context.startKoin


class FDJApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // Dependency injection
        // Koin module can be replaced with Hilt (when a stable release is available) or Dagger
        startKoin {
            modules(appModule)
        }
    }
}