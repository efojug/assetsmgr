package com.efojug.assetsmgr

import android.app.Application
import com.efojug.assetsmgr.manager.AssetsManager
import com.efojug.assetsmgr.util.extension.dataStore
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.dsl.module

class Application : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(applicationContext)

            modules(
                module {
                    single { AssetsManager(applicationContext.dataStore) }
                }
            )
        }
    }
}