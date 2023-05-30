package kids.baba.mobile.data.datasource.mypage

import kids.baba.mobile.domain.model.*
import kotlinx.coroutines.flow.Flow

interface MyPageRemoteDataSource {

    suspend fun loadMyPageGroup(): Result<GroupResponse>

    suspend fun loadBabyProfile(babyId: String): Result<BabyProfileResponse>

    suspend fun addGroup(myPageGroup: MyPageGroup): Result<Unit>

    suspend fun editProfile(profile: Profile): Result<Unit>

    suspend fun editBabyName(babyId: String, name: String): Result<Unit>

    suspend fun addMyBaby(baby: MyBaby): Result<Unit>

    suspend fun addBabyWithInviteCode(inviteCode: InviteCode): Result<Unit>

    suspend fun deleteBaby(babyId: String)

    suspend fun patchGroup(groupName: String, group: GroupInfo): Result<Unit>

    suspend fun deleteGroup(groupName: String): Result<Unit>

    suspend fun patchMember(memberId: String, relation: GroupMemberInfo): Result<Unit>

    suspend fun deleteGroupMember(memberId: String)

    suspend fun getInvitationInfo(inviteCode: String): Result<BabiesInfoResponse>

}