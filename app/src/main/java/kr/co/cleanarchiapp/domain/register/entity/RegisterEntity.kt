package kr.co.cleanarchiapp.domain.register.entity

data class RegisterEntity(
    val id: Int,
    val name: String,
    val email: String,
    val token: String
)