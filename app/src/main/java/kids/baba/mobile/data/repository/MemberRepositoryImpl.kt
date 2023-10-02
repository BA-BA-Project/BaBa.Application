package kids.baba.mobile.data.repository

import kids.baba.mobile.data.datasource.member.MemberRemoteDataSource
import kids.baba.mobile.domain.model.SignUpRequestWithBabiesInfo
import kids.baba.mobile.domain.model.SignUpRequestWithInviteCode
import kids.baba.mobile.domain.repository.MemberRepository
import javax.inject.Inject

class MemberRepositoryImpl @Inject constructor(private val memberRemoteDataSource: MemberRemoteDataSource) :
    MemberRepository {
    override suspend fun getMe(accessToken: String) = memberRemoteDataSource.getMe(accessToken)

    // TODO: 제거
    override suspend fun signUpWithBabiesInfo(
        signToken: String,
        signUpRequestWithBabiesInfo: SignUpRequestWithBabiesInfo
    ) = memberRemoteDataSource.signUpWithBabiesInfo(signToken, signUpRequestWithBabiesInfo)


    override suspend fun signUpWithInviteCode(
        signToken: String,
        signUpRequestWithInviteCode: SignUpRequestWithInviteCode
    ) = memberRemoteDataSource.signUpWithInviteCode(signToken, signUpRequestWithInviteCode)
}
