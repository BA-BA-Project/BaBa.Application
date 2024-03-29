package kids.baba.mobile.data.repository

import kids.baba.mobile.data.datasource.mypage.MyPageRemoteDataSource
import kids.baba.mobile.domain.model.*
import kids.baba.mobile.domain.repository.MyPageRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MyPageRepositoryImpl @Inject constructor(private val dataSource: MyPageRemoteDataSource) :
    MyPageRepository {
    override suspend fun loadMyPageGroup(): Result<GroupResponse> = dataSource.loadMyPageGroup()

    override suspend fun loadBabyProfile(babyId: String) =
        dataSource.loadBabyProfile(babyId = babyId)

    override suspend fun addGroup(myPageGroup: MyPageGroup) =
        dataSource.addGroup(myPageGroup = myPageGroup)


    override suspend fun editProfile(profile: Profile): Result<Unit> {
        return dataSource.editProfile(profile = profile)
    }

    override suspend fun editBabyName(babyId: String, name: String) =
        dataSource.editBabyName(babyId = babyId, name = name)

    override suspend fun addMyBaby(baby: MyBaby): Result<Unit> {
        return dataSource.addMyBaby(baby = baby)
    }

    override suspend fun addBabyWithInviteCode(inviteCode: InviteCode) =
        dataSource.addBabyWithInviteCode(inviteCode = inviteCode)

    override suspend fun deleteBaby(babyId: String) =
        dataSource.deleteBaby(babyId = babyId)

    override suspend fun patchGroup(groupName: String, group: GroupInfo) =
        dataSource.patchGroup(groupName = groupName, group = group)

    override suspend fun deleteGroup(groupName: String) = dataSource.deleteGroup(groupName = groupName)

    override suspend fun patchMember(memberId: String, relation: GroupMemberInfo) =
        dataSource.patchMember(memberId = memberId, relation = relation)

    override suspend fun deleteGroupMember(memberId: String) =
        dataSource.deleteGroupMember(memberId = memberId)


    override suspend fun getInvitationInfo(inviteCode: String): Result<BabiesInfoResponse> =
        dataSource.getInvitationInfo(inviteCode = inviteCode)

    override suspend fun makeInviteCode(relationInfo: RelationInfo): Result<InviteCode> =
        dataSource.makeInviteCode(relationInfo = relationInfo)

}