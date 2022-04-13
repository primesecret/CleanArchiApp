package kr.co.cleanarchiapp.domain.login.usecase

import kotlinx.coroutines.flow.Flow
import kr.co.cleanarchiapp.data.common.utils.WrappedResponse
import kr.co.cleanarchiapp.data.login.remote.dto.LoginRequest
import kr.co.cleanarchiapp.data.login.remote.dto.LoginResponse
import kr.co.cleanarchiapp.domain.common.base.BaseResult
import kr.co.cleanarchiapp.domain.login.LoginRepository
import kr.co.cleanarchiapp.domain.login.entity.LoginEntity
import javax.inject.Inject

class LoginUseCase @Inject constructor(private val loginRepository: LoginRepository) {
    suspend fun execute(loginRequest: LoginRequest):
            Flow<BaseResult<LoginEntity, WrappedResponse<LoginResponse>>> {
        return loginRepository.login(loginRequest)
    }

}