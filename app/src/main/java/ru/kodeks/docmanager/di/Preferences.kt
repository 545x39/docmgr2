@file:Suppress("DEPRECATION")

package ru.kodeks.docmanager.di

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import dagger.Component
import dagger.Module
import dagger.Provides
import ru.kodeks.docmanager.util.DocManagerApp

@Component(modules = [PreferencesModule::class])
interface PreferencesComponent {

    fun preferences(): SharedPreferences
}

@Module
class PreferencesModule {

    @Provides
    fun providePreferences(context: Context): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(context)
    }

    @Provides
    fun provideContext(): Context {
        return DocManagerApp.instance
    }
}