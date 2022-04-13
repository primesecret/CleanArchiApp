package kr.co.cleanarchiapp.domain.register.usecase

import kotlinx.coroutines.flow.Flow
import kr.co.cleanarchiapp.data.common.utils.WrappedResponse
import kr.co.cleanarchiapp.data.register.remote.dto.RegisterRequest
import kr.co.cleanarchiapp.data.register.remote.dto.RegisterResponse
import kr.co.cleanarchiapp.domain.common.base.BaseResult
import kr.co.cleanarchiapp.domain.register.RegisterRepository
import kr.co.cleanarchiapp.domain.register.entity.RegisterEntity
import javax.inject.Inject

class RegisterUseCase @Inject constructor(private val registerRepository: RegisterRepository) {

    suspend fun invoke(registerRequest: RegisterRequest)
            : Flow<BaseResult<RegisterEntity, WrappedResponse<RegisterResponse>>> {
        return registerRepository.register(registerRequest)
    }
}