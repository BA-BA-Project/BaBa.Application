package kids.baba.mobile.domain.usecase

import kids.baba.mobile.core.constant.PrefsKey
import kids.baba.mobile.core.error.MemberNotFoundException
import kids.baba.mobile.core.error.TokenEmptyException
import kids.baba.mobile.core.utils.EncryptedPrefs
import kids.baba.mobile.domain.model.ApiResult
import kids.baba.mobile.domain.model.MemberModel
import kids.baba.mobile.domain.repository.MemberRepository
import javax.inject.Inject

class GetMemberUseCase @Inject constructor(private val memberRepository: MemberRepository) {
    private val tag = "GetMemberUseCase"

    suspend fun getMe(callFromServer: Boolean = true): ApiResult<MemberModel> {
        val member = runCatching { EncryptedPrefs.getMember(PrefsKey.MEMBER_KEY) }.getOrNull()
        return if (member == null) {
            if (callFromServer) {
                getMeNoPref()
            }
            ApiResult.Unexpected(MemberNotFoundException())
        } else {
            ApiResult.Success(member)
        }
    }

    suspend fun getMeNoPref(): ApiResult<MemberModel> {
        val result = memberRepository.getMe()
        if (result is ApiResult.Success) {
            EncryptedPrefs.putMember(PrefsKey.MEMBER_KEY, result.data)
            return result
        }
        return ApiResult.Unexpected(TokenEmptyException("accessToken이 존재하지 않음."))

    }
}
