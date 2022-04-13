package kr.co.cleanarchiapp.data.login.remote.api

import kr.co.cleanarchiapp.data.common.utils.WrappedResponse
import kr.co.cleanarchiapp.data.login.remote.dto.LoginRequest
import kr.co.cleanarchiapp.data.login.remote.dto.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginApi {
    @POST("auth/login")
    suspend fun login(@Body loginRequest: LoginRequest) : Response<WrappedResponse<LoginResponse>>
}