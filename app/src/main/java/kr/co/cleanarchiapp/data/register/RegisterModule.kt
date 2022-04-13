package kr.co.cleanarchiapp.data.register

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kr.co.cleanarchiapp.data.common.module.NetworkModule
import kr.co.cleanarchiapp.data.register.remote.api.RegisterApi
import kr.co.cleanarchiapp.data.register.repository.RegisterRepositoryImpl
import kr.co.cleanarchiapp.domain.register.RegisterRepository
import retrofit2.Retrofit
import javax.inject.Singleton

@Module(includes = [NetworkModule::class])
@InstallIn(SingletonComponent::class)
class RegisterModule {
    @Singleton
    @Provides
    fun provideRegisterApi(retrofit: Retrofit) : RegisterApi {
        return retrofit.create(RegisterApi::class.java)
    }

    @Singleton
    @Provides
    fun provideRegisterRepository(registerApi: RegisterApi) : RegisterRepository {
        return RegisterRepositoryImpl(registerApi)
    }
}