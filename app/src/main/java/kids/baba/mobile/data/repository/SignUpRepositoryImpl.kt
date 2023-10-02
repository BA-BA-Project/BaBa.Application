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
        /*signToken: String,*/
        signUpRequestWithBabiesInfo: SignUpRequestWithBabiesInfo
    ) = signUpRemoteDataSource.signUpWithBabiesInfo(/*signToken,*/ signUpRequestWithBabiesInfo)

    override suspend fun signUpWithInviteCode(
        /*signToken: String,*/
        signUpRequestWithInviteCode: SignUpRequestWithInviteCode
    ) = signUpRemoteDataSource.signUpWithInviteCode(/*signToken,*/ signUpRequestWithInviteCode)

}
