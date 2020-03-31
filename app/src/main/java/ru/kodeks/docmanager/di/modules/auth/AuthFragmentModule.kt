package ru.kodeks.docmanager.di.modules.auth

import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import ru.kodeks.docmanager.network.api.FlowableApi

@Module
class AuthFragmentModule {

    @Provides
    fun provideApi(retrofit: Retrofit): FlowableApi {
        return retrofit.create(FlowableApi::class.java)
    }
}