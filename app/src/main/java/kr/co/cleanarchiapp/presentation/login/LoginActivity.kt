package kr.co.cleanarchiapp.presentation.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kr.co.cleanarchiapp.R
import kr.co.cleanarchiapp.data.common.utils.WrappedResponse
import kr.co.cleanarchiapp.data.login.remote.dto.LoginRequest
import kr.co.cleanarchiapp.data.login.remote.dto.LoginResponse
import kr.co.cleanarchiapp.databinding.ActivityLoginBinding
import kr.co.cleanarchiapp.domain.login.entity.LoginEntity
import kr.co.cleanarchiapp.infra.utils.SharedPrefs
import kr.co.cleanarchiapp.presentation.common.extension.isEmail
import kr.co.cleanarchiapp.presentation.common.extension.showGenericAlertDialog
import kr.co.cleanarchiapp.presentation.common.extension.showToast
import kr.co.cleanarchiapp.presentation.main.MainActivity
import kr.co.cleanarchiapp.presentation.register.RegisterActivity
import javax.inject.Inject

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModels()

    @Inject
    lateinit var sharedPrefs: SharedPrefs

    private val openRegisterActivity = registerForActivityResult(
                                            ActivityResultContracts.StartActivityForResult()
                                        ) { result ->
        if (result.resultCode == RESULT_OK) {
            goToMainActivity()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        login()
        observe()
    }

    private fun login(){
        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()
            // 정합성 체크
            if(validate(email, password)){
                val loginRequest = LoginRequest(email, password)
                viewModel.login(loginRequest)
            }
        }
    }

    private fun validate(email: String, password: String) : Boolean{
        resetAllInputError()
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
        setEmailError(null)
        setPasswordError(null)
    }

    private fun setEmailError(e : String?){
        binding.emailInput.error = e
    }

    private fun setPasswordError(e: String?){
        binding.passwordInput.error = e
    }

    private fun observe(){
        viewModel.mState
            .flowWithLifecycle(lifecycle,  Lifecycle.State.STARTED)
            .onEach { state -> handleStateChange(state) }
            .launchIn(lifecycleScope)
    }

    private fun handleStateChange(state: LoginActivityState){
        when(state){
            is LoginActivityState.Init -> Unit
            is LoginActivityState.ErrorLogin -> handleErrorLogin(state.rawResponse)
            is LoginActivityState.SuccessLogin -> handleSuccessLogin(state.loginEntity)
            is LoginActivityState.ShowToast -> showToast(state.message)
            is LoginActivityState.IsLoading -> handleLoading(state.isLoading)
        }
    }
    // 로그인 에러 시
    private fun handleErrorLogin(response: WrappedResponse<LoginResponse>){
        showGenericAlertDialog(response.message)
    }
    // 로그인 처리 중
    private fun handleLoading(isLoading: Boolean){
        binding.loginButton.isEnabled = !isLoading
        binding.registerButton.isEnabled = !isLoading
        binding.loadingProgressBar.isIndeterminate = isLoading
        if(!isLoading){
            binding.loadingProgressBar.progress = 0
        }
    }
    // 로그인 성공 시
    private fun handleSuccessLogin(loginEntity: LoginEntity){
        sharedPrefs.saveToken(loginEntity.token)
        showToast("Welcome ${loginEntity.name}")
        goToMainActivity()
    }

    // 회원가입 화면 호출
    private fun goToRegisterActivity(){
        binding.registerButton.setOnClickListener {
            openRegisterActivity.launch(Intent(this@LoginActivity, RegisterActivity::class.java))
        }
    }
    // 메인으로 이동
    private fun goToMainActivity(){
        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
        finish()
    }

}