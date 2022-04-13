package kr.co.cleanarchiapp.data.register.remote.api

import kr.co.cleanarchiapp.data.common.utils.WrappedResponse
import kr.co.cleanarchiapp.data.register.remote.dto.RegisterRequest
import kr.co.cleanarchiapp.data.register.remote.dto.RegisterResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface RegisterApi {
    @POST("auth/register")
    suspend fun register(@Body registerRequest: RegisterRequest)
            : Response<WrappedResponse<RegisterResponse>>
}