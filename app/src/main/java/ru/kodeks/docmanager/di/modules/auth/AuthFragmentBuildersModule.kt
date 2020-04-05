package ru.kodeks.docmanager.di.modules.auth

import dagger.Module
import dagger.android.ContributesAndroidInjector
import ru.kodeks.docmanager.di.modules.auth.base.BaseAuthViewModelModule
import ru.kodeks.docmanager.di.modules.auth.form.AuthFormViewModelModule
import ru.kodeks.docmanager.di.modules.auth.progress.AuthProgressViewModelModule
import ru.kodeks.docmanager.ui.fragments.auth.base.StartFragment
import ru.kodeks.docmanager.ui.fragments.auth.form.AuthFormFragment
import ru.kodeks.docmanager.ui.fragments.auth.progress.AuthProgressFragment

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