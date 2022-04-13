package kr.co.cleanarchiapp.presentation.register

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kr.co.cleanarchiapp.R
import kr.co.cleanarchiapp.data.common.utils.WrappedResponse
import kr.co.cleanarchiapp.data.register.remote.dto.RegisterRequest
import kr.co.cleanarchiapp.data.register.remote.dto.RegisterResponse
import kr.co.cleanarchiapp.databinding.ActivityRegisterBinding
import kr.co.cleanarchiapp.domain.register.entity.RegisterEntity
import kr.co.cleanarchiapp.infra.utils.SharedPrefs
import kr.co.cleanarchiapp.presentation.common.extension.isEmail
import kr.co.cleanarchiapp.presentation.common.extension.showGenericAlertDialog
import kr.co.cleanarchiapp.presentation.common.extension.showToast
import javax.inject.Inject

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {

    private lateinit var binding : ActivityRegisterBinding    // 화면연결 용도
    private val viewModel: RegisterViewModel by viewModels()  // data연결 용도
    @Inject
    lateinit var sharedPrefs: SharedPrefs                     // 메모리 연결 용도

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        back()
        register()
        observe()
    }

    // 뒤로가기
    private fun back(){
        binding.backButton.setOnClickListener {
            finish()
        }
    }

    // 회원가입
    private fun register() {

        binding.registerButton.setOnClickListener {
            val name = binding.nameEditText.text.toString().trim()
            val email = binding.emailEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()

            // name, email, password 값 정합성 체크
            if(validate(name, email, password)){
                viewModel.register(RegisterRequest(name, email, password))
            }
        }
    }

    // 정합성 체크
    private fun validate(name: String, email: String, password: String) :  Boolean {
        resetAllInputError()

        if(name.isEmpty()){
            setNameError(getString(R.string.error_name_not_valid))
            return false
        }

        if(!email.isEmail()){
            setEmailError(getString(R.string.error_email_not_valid))
            return false
        }

        if(password.length < 8){
            setPasswordError(getString(R.string.error_password_not_valid))
            return false
        }
        return true
    }

    private fun resetAllInputError(){
        setNameError(null)
        setEmailError(null)
        setPasswordError(null)
    }

    private fun setNameError(e: String?){
        binding.nameInput.error = e
    }

    private fun setEmailError(e: String?){
        binding.emailInput.error = e
    }

    private fun setPasswordError(e: String?){
        binding.passwordInput.error = e
    }

    // 데이터를 감시하는 함수 (즉 회원가입 결과가 온다면)
    private fun observe(){
        viewModel.mState
            .flowWithLifecycle(lifecycle,  Lifecycle.State.STARTED)
            .onEach { state -> handleStateChange(state) }
            .launchIn(lifecycleScope)
    }

    private fun handleStateChange(state: RegisterActivityState){
        when(state){
            is RegisterActivityState.ShowToast -> showToast(state.message)
            is RegisterActivityState.IsLoading -> handleLoading(state.isLoading)
            is RegisterActivityState.SuccessRegister -> handleSuccessRegister(state.registerEntity)
            is RegisterActivityState.ErrorRegister -> handleErrorRegister(state.rawResponse)
            is RegisterActivityState.Init -> Unit
        }
    }
    // 회원가입 성공 시
    private fun handleSuccessRegister(registerEntity: RegisterEntity){
        sharedPrefs.saveToken(registerEntity.token)
        setResult(RESULT_OK)
        finish()
    }

    // 회원가입 실패 시
    private fun handleErrorRegister(httpResponse: WrappedResponse<RegisterResponse>){
        showGenericAlertDialog(httpResponse.message)
    }

    // 회원가입 요청 진행 중
    private fun handleLoading(isLoading: Boolean){
        binding.registerButton.isEnabled = !isLoading
        binding.backButton.isEnabled = !isLoading
        binding.loadingProgressBar.isIndeterminate = isLoading
        if(!isLoading){
            binding.loadingProgressBar.progress = 0
        }
    }

}