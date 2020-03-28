package ru.kodeks.docmanager.di.modules.auth

import dagger.Module
import dagger.android.ContributesAndroidInjector
import ru.kodeks.docmanager.ui.fragments.auth.AuthFragment

/** Модуль для предоставления зависимостей всех фрагментов
 * внутри подкомпонента MainActivity.*/
@Module
abstract class AuthFragmentBuildersModule {

    /** Добавляет подкомпонент фрагемент формы авторизации в MainActivity.*/
    @ContributesAndroidInjector(
        modules = [AuthViewModelModule::class,
            AuthFragmentModule::class]
    )
    abstract fun contributeAuthFormFragment(): AuthFragment
}