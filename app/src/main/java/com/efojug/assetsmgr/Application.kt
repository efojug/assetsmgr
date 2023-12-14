package com.efojug.assetsmgr

import android.app.Application
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.efojug.assetsmgr.manager.ExpenseManager
import com.efojug.assetsmgr.util.extension.dataStore
import com.efojug.assetsmgr.util.store.StoreHelper
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.dsl.module


class Application : Application() {

    private var mSharedPreferences: SharedPreferences? = null

    override fun onCreate() {
        super.onCreate()

        // 创建 SharedPreferences 对象
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        startKoin {
            androidContext(applicationContext)

            modules(
                module {
                    single { applicationContext }

                    single { StoreHelper(get()) }
                    single { ExpenseManager(get()) }
                }
            )
        }
    }

    fun getSharedPreferences(): SharedPreferences? {
        return mSharedPreferences
    }
}