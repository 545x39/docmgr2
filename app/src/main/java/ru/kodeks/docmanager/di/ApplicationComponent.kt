package ru.kodeks.docmanager.di

import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import ru.kodeks.docmanager.DocManagerApp
import ru.kodeks.docmanager.di.module.ApplicationModule
import ru.kodeks.docmanager.di.module.viewmodel.ViewModelFactoryModule
import ru.kodeks.docmanager.di.module.viewmodel.main.ActivitySubcomponentsModule
import ru.kodeks.docmanager.di.module.work.checkstate.CheckStateWorkerModule
import ru.kodeks.docmanager.di.module.work.signaturestamp.GetQualifiedSignatureStampWorkerModule
import ru.kodeks.docmanager.di.module.work.signaturestamp.GetSimpleSignatureStampWorkerModule
import ru.kodeks.docmanager.di.module.work.sync.ParseResponseWorkerModule
import ru.kodeks.docmanager.di.module.work.sync.SyncRequestWorkerModule
import ru.kodeks.docmanager.work.checkstate.CheckStateWorker
import javax.inject.Singleton

@Singleton
@Component(
    modules = [AndroidSupportInjectionModule::class,
        ApplicationModule::class,
        ActivitySubcomponentsModule::class,
        ViewModelFactoryModule::class,
        CheckStateWorkerModule::class,
        SyncRequestWorkerModule::class,
        GetSimpleSignatureStampWorkerModule::class,
        GetQualifiedSignatureStampWorkerModule::class,
        ParseResponseWorkerModule::class
    ]
)
interface ApplicationComponent : AndroidInjector<DocManagerApp> {

    @Component.Builder
    interface Builder {

        /** Связывает экземпляр приложения с компонентом.*/
        @BindsInstance
        fun application(application: DocManagerApp): Builder

        fun build(): ApplicationComponent
    }

    fun injectCheckStateWorker(worker: CheckStateWorker)

}