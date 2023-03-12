package kids.baba.mobile.data.repository

import kids.baba.mobile.data.datasource.member.MemberRemoteDataSource
import kids.baba.mobile.domain.model.SignUpRequest
import kids.baba.mobile.domain.repository.MemberRepository
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MemberRepositoryImpl @Inject constructor(private val memberRemoteDataSource: MemberRemoteDataSource) :
    MemberRepository {
    override suspend fun getMe(accessToken: String) = flow {
        memberRemoteDataSource.getMe(accessToken).catch {
            throw it
        }.collect {
            emit(it)
        }
    }

    override suspend fun signUp(
        signToken: String,
        signUpRequest: SignUpRequest
    ) = flow {
        memberRemoteDataSource.signUp(signToken, signUpRequest).collect {
            emit(it)
        }
    }
}
