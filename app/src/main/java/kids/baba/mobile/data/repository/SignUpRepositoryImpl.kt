package kids.baba.mobile.data.repository

import kids.baba.mobile.data.datasource.signup.SignUpRemoteDataSource
import kids.baba.mobile.domain.model.SignUpRequestWithBabiesInfo
import kids.baba.mobile.domain.model.SignUpRequestWithInviteCode
import kids.baba.mobile.domain.repository.SignUpRepository
import javax.inject.Inject

class SignUpRepositoryImpl @Inject constructor(
    private val signUpRemoteDataSource: SignUpRemoteDataSource
) : SignUpRepository {
    override suspend fun signUpWithBabiesInfo(
        signUpRequestWithBabiesInfo: SignUpRequestWithBabiesInfo
    ) = signUpRemoteDataSource.signUpWithBabiesInfo(signUpRequestWithBabiesInfo)

    override suspend fun signUpWithInviteCode(
        signUpRequestWithInviteCode: SignUpRequestWithInviteCode
    ) = signUpRemoteDataSource.signUpWithInviteCode(signUpRequestWithInviteCode)

}
