package kr.co.cleanarchiapp.data.register.repository

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kr.co.cleanarchiapp.data.common.utils.WrappedResponse
import kr.co.cleanarchiapp.data.register.remote.api.RegisterApi
import kr.co.cleanarchiapp.data.register.remote.dto.RegisterRequest
import kr.co.cleanarchiapp.data.register.remote.dto.RegisterResponse
import kr.co.cleanarchiapp.domain.common.base.BaseResult
import kr.co.cleanarchiapp.domain.register.RegisterRepository
import kr.co.cleanarchiapp.domain.register.entity.RegisterEntity
import javax.inject.Inject

class RegisterRepositoryImpl @Inject constructor(private val registerApi: RegisterApi) :
    RegisterRepository {
    override suspend fun register(registerRequest: RegisterRequest): Flow<BaseResult<RegisterEntity, WrappedResponse<RegisterResponse>>> {
        return flow {
            val response = registerApi.register(registerRequest)
            if (response.isSuccessful){
                val body = response.body()!!
                val registerEntity = RegisterEntity(body.data?.id!!, body.data?.name!!, body.data?.email!!, body.data?.token!!)
                emit(BaseResult.Success(registerEntity))
            }else{
                val type = object : TypeToken<WrappedResponse<RegisterResponse>>(){}.type
                val err : WrappedResponse<RegisterResponse> = Gson().fromJson(response.errorBody()!!.charStream(), type)
                err.code = response.code()
                emit(BaseResult.Error(err))
            }
        }
    }
}