package ru.kodeks.docmanager.di.module.viewmodel.auth

import dagger.Module
import dagger.android.ContributesAndroidInjector
import ru.kodeks.docmanager.di.module.viewmodel.auth.base.BaseAuthViewModelModule
import ru.kodeks.docmanager.di.module.viewmodel.auth.form.AuthFormViewModelModule
import ru.kodeks.docmanager.di.module.viewmodel.auth.progress.AuthProgressViewModelModule
import ru.kodeks.docmanager.ui.fragments.auth.form.AuthFormFragment
import ru.kodeks.docmanager.ui.fragments.auth.progress.AuthProgressFragment
import ru.kodeks.docmanager.ui.fragments.auth.start.StartFragment

/** Модуль для предоставления зависимостей всех фрагментов
 * внутри подкомпонента MainActivity.*/
@Module
interface AuthFragmentBuildersModule {

    @AuthScope
    @ContributesAndroidInjector(
        modules = [BaseAuthViewModelModule::class]
    )
    fun contributeMainFragment(): StartFragment

    @AuthScope
    @ContributesAndroidInjector(
        modules = [AuthFormViewModelModule::class,
            AuthFragmentModule::class]
    )
    fun contributeAuthFormFragment(): AuthFormFragment

    @AuthScope
    @ContributesAndroidInjector(
        modules = [AuthProgressViewModelModule::class]
    )
    fun contributeAuthProgressFragment(): AuthProgressFragment
}