package kids.baba.mobile.domain.usecase

import android.util.Log
import kids.baba.mobile.core.constant.PrefsKey
import kids.baba.mobile.core.utils.EncryptedPrefs
import kids.baba.mobile.domain.repository.MemberRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class GetMemberUseCase @Inject constructor(private val memberRepository: MemberRepository) {
    private val tag = "GetMemberUseCase"

    //TODO 수정되어야 함
    suspend fun getMe() = runCatching {
        val accessToken = EncryptedPrefs.getString(PrefsKey.ACCESS_TOKEN_KEY)

        if (accessToken.isEmpty()) {
            val msg = "accessToken이 존재하지 않음."
            Log.e(tag, msg)
            throw Throwable(msg)
        }

        memberRepository.getMe(accessToken).first()
    }
}
