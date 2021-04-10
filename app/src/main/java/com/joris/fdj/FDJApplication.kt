package com.joris.fdj

import android.app.Application
import com.joris.data.di.dataModule
import com.joris.presentation.di.presentationModule
import org.koin.core.context.startKoin


class FDJApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            modules(dataModule, presentationModule)
        }
    }
}