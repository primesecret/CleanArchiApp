package kr.co.cleanarchiapp.domain.login

import kotlinx.coroutines.flow.Flow
import kr.co.cleanarchiapp.data.common.utils.WrappedResponse
import kr.co.cleanarchiapp.data.login.remote.dto.LoginRequest
import kr.co.cleanarchiapp.data.login.remote.dto.LoginResponse
import kr.co.cleanarchiapp.domain.common.base.BaseResult
import kr.co.cleanarchiapp.domain.login.entity.LoginEntity

interface LoginRepository {
    suspend fun login(loginRequest: LoginRequest)
        : Flow<BaseResult<LoginEntity, WrappedResponse<LoginResponse>>>
}