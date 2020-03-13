package ru.kodeks.docmanager.di

import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import ru.kodeks.docmanager.util.DocManagerApp
import javax.inject.Singleton

@Singleton
@Component(
    modules = [AndroidSupportInjectionModule::class,
        AuthActivityModule::class,
        ApplicationModule::class]
)
interface ApplicationComponent : AndroidInjector<DocManagerApp> {


    @Component.Builder
    interface Builder {

        /** Связывает экземпляр приложения с компонентом.*/
        @BindsInstance
        fun application(application: DocManagerApp): Builder

        fun build(): ApplicationComponent
    }
}