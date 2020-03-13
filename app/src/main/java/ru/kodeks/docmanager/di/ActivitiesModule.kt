package ru.kodeks.docmanager.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import ru.kodeks.docmanager.ui.AuthActivity
import ru.kodeks.docmanager.ui.BaseActivity

/** Модуль для всех активностей/фрагментов, которые потребуют внедрения зависимостей.*/
@Module
abstract class AuthActivityModule {

    @ContributesAndroidInjector
    abstract fun contributeBaseActivity(): BaseActivity

    @ContributesAndroidInjector
    abstract fun contributeAuthActivity(): AuthActivity

}