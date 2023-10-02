package kids.baba.mobile.domain.usecase

import android.util.Log
import kids.baba.mobile.core.constant.PrefsKey
import kids.baba.mobile.core.error.TokenEmptyException
import kids.baba.mobile.core.utils.EncryptedPrefs
import kids.baba.mobile.domain.model.ApiResult
import kids.baba.mobile.domain.model.MemberModel
import kids.baba.mobile.domain.repository.MemberRepository
import javax.inject.Inject

class GetMemberUseCase @Inject constructor(private val memberRepository: MemberRepository) {
    private val tag = "GetMemberUseCase"

    suspend fun getMe(): ApiResult<MemberModel> {
        val member = runCatching { EncryptedPrefs.getMember(PrefsKey.MEMBER_KEY) }.getOrNull()
        return if (member == null) {
            getMeNoPref()
        } else {
            ApiResult.Success(member)
        }
    }

    suspend fun getMeNoPref(): ApiResult<MemberModel> {
        val accessToken = EncryptedPrefs.getString(PrefsKey.ACCESS_TOKEN_KEY)

        return if (accessToken.isEmpty()) {
            val msg = "accessToken이 존재하지 않음."
            Log.e(tag, msg)
            ApiResult.Unexpected(TokenEmptyException(msg))
        } else {
            val result = memberRepository.getMe(accessToken)
            if (result is ApiResult.Success) {
                EncryptedPrefs.putMember(PrefsKey.MEMBER_KEY, result.data)
            }
            result
        }

    }
}
