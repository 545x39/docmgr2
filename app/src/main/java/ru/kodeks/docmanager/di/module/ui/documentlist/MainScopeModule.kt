package ru.kodeks.docmanager.di.module.ui.documentlist

import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import ru.kodeks.docmanager.db.Database
import ru.kodeks.docmanager.di.module.ui.MainScope
import ru.kodeks.docmanager.repository.WorkbenchRepository

@Module
class MainScopeModule {

    @MainScope
    @Provides
    fun bindWorkbenchRepository(database: Database, preferences: SharedPreferences) =
        WorkbenchRepository(database, preferences)
}