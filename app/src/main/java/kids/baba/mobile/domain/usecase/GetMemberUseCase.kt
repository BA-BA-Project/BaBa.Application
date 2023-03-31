package kids.baba.mobile.domain.usecase

import android.util.Log
import kids.baba.mobile.core.constant.PrefsKey
import kids.baba.mobile.core.error.MemberNotFoundException
import kids.baba.mobile.core.error.TokenEmptyException
import kids.baba.mobile.core.utils.EncryptedPrefs
import kids.baba.mobile.domain.repository.MemberRepository
import kids.baba.mobile.presentation.mapper.toPresentation
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class GetMemberUseCase @Inject constructor(private val memberRepository: MemberRepository) {
    private val tag = "GetMemberUseCase"

    suspend fun getMe() =
        runCatching { EncryptedPrefs.getMember(PrefsKey.MEMBER_KEY).toPresentation() }.getOrElse {
            if (it is MemberNotFoundException) {
                val accessToken = EncryptedPrefs.getString(PrefsKey.ACCESS_TOKEN_KEY)

                if (accessToken.isEmpty()) {
                    val msg = "accessToken이 존재하지 않음."
                    Log.e(tag, msg)
                    throw TokenEmptyException(msg)
                } else {
                    memberRepository.getMe(accessToken).first().toPresentation()
                }
            } else {
                throw it
            }
        }
}
