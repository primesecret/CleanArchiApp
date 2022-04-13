package kr.co.cleanarchiapp.data.login

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kr.co.cleanarchiapp.data.common.module.NetworkModule
import kr.co.cleanarchiapp.data.login.remote.api.LoginApi
import kr.co.cleanarchiapp.data.login.repository.LoginRepositoryImpl
import kr.co.cleanarchiapp.domain.login.LoginRepository
import retrofit2.Retrofit
import javax.inject.Singleton


@Module(includes = [NetworkModule::class])
@InstallIn(SingletonComponent::class)
class LoginModule {

    @Singleton
    @Provides
    fun provideLoginApi(retrofit: Retrofit) : LoginApi {
        return retrofit.create(LoginApi::class.java)
    }

    @Singleton
    @Provides
    fun provideLoginRepository(loginApi: LoginApi) : LoginRepository {
        return LoginRepositoryImpl(loginApi)
    }


}