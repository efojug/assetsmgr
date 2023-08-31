package com.efojug.assetsmgr.util.extension

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore

val Context.dataStore by preferencesDataStore("data")