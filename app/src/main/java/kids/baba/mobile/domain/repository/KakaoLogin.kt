package kids.baba.mobile.domain.repository

interface KakaoLogin {
    suspend fun login(): Result<String>
}