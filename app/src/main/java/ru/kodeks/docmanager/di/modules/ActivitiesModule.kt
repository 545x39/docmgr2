package ru.kodeks.docmanager.di.modules

import dagger.Module
import dagger.android.ContributesAndroidInjector
import ru.kodeks.docmanager.ui.BaseActivity
import ru.kodeks.docmanager.ui.auth.AuthActivity

/** Модуль для всех активностей/фрагментов, которые потребуют внедрения зависимостей.*/
@Module
abstract class ActivitiesModule {

    @ContributesAndroidInjector
    abstract fun contributeBaseActivity(): BaseActivity

    @ContributesAndroidInjector
    abstract fun contributeAuthActivity(): AuthActivity

}