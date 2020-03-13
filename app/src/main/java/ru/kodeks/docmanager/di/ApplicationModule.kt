package ru.kodeks.docmanager.di

import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import ru.kodeks.docmanager.util.AppExecutors
import ru.kodeks.docmanager.util.DocManagerApp
import javax.inject.Inject

/** Модуль для всего, что будет использоваться глобально на уровне приложения: Room, Retrofit  и т.д.*/
@Module
class ApplicationModule {

    @Inject
    lateinit var app: DocManagerApp

    @Inject
    lateinit var executors: AppExecutors
//
//    @Inject
//    lateinit var preferences: SharedPreferences

    @Provides
    fun providePreferences(): SharedPreferences {
        return DaggerPreferencesComponent.create().preferences()
    }

}