package ru.kodeks.docmanager.di.modules.auth

import dagger.Module
import dagger.android.ContributesAndroidInjector
import ru.kodeks.docmanager.di.modules.auth.form.AuthFormViewModelModule
import ru.kodeks.docmanager.ui.fragments.auth.MainFragment
import ru.kodeks.docmanager.ui.fragments.auth.form.AuthFormFragment

/** Модуль для предоставления зависимостей всех фрагментов
 * внутри подкомпонента MainActivity.*/
@Module
abstract class AuthFragmentBuildersModule {

    @ContributesAndroidInjector(
        modules = [MainViewModelModule::class]
    )
    abstract fun contributeMainFragment(): MainFragment

    @ContributesAndroidInjector(
        modules = [AuthFormViewModelModule::class,
            AuthFragmentModule::class]
    )
    abstract fun contributeAuthFormFragment(): AuthFormFragment
}