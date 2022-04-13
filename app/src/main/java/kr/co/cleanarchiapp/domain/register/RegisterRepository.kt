package kr.co.cleanarchiapp.domain.register

import kotlinx.coroutines.flow.Flow
import kr.co.cleanarchiapp.data.common.utils.WrappedResponse
import kr.co.cleanarchiapp.data.register.remote.dto.RegisterRequest
import kr.co.cleanarchiapp.data.register.remote.dto.RegisterResponse
import kr.co.cleanarchiapp.domain.common.base.BaseResult
import kr.co.cleanarchiapp.domain.register.entity.RegisterEntity

interface RegisterRepository {
    suspend fun register(registerRequest: RegisterRequest)
            : Flow<BaseResult<RegisterEntity, WrappedResponse<RegisterResponse>>>

}