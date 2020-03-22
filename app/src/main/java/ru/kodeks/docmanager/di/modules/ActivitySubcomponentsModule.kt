package ru.kodeks.docmanager.di.modules

import dagger.Module
import dagger.android.ContributesAndroidInjector
import ru.kodeks.docmanager.di.modules.auth.AuthViewModelModule
import ru.kodeks.docmanager.ui.auth.AuthActivity
import ru.kodeks.docmanager.ui.auth.AuthModule

/** Модуль для всех активностей/фрагментов, которые потребуют внедрения зависимостей.*/
@Module
abstract class ActivitySubcomponentsModule {

//    @ContributesAndroidInjector
//    abstract fun contributeBaseActivity(): BaseActivity

    /** Таким образом модуль будет доступен не во всём приложении, а толькот в нужной активности.*/
    /** Аннотация @ContributesAndroidInjector генерирует @Subcomponent для этой активности!*/
    @ContributesAndroidInjector(
        modules = [AuthViewModelModule::class,
            AuthModule::class]
    )
    abstract fun contributeAuthActivity(): AuthActivity

}