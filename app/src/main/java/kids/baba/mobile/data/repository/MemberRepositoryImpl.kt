package kids.baba.mobile.data.repository

import kids.baba.mobile.data.datasource.member.MemberRemoteDataSource
import kids.baba.mobile.domain.repository.MemberRepository
import javax.inject.Inject

class MemberRepositoryImpl @Inject constructor(private val memberRemoteDataSource: MemberRemoteDataSource) :
    MemberRepository {
    override suspend fun getMe(accessToken: String) = memberRemoteDataSource.getMe(accessToken)
}
