package kids.baba.mobile.data.datasource.mypage

import kids.baba.mobile.domain.model.BabyProfileResponse
import kids.baba.mobile.domain.model.GroupInfo
import kids.baba.mobile.domain.model.GroupMemberInfo
import kids.baba.mobile.domain.model.GroupResponse
import kids.baba.mobile.domain.model.InviteCode
import kids.baba.mobile.domain.model.MyBaby
import kids.baba.mobile.domain.model.MyPageGroup
import kids.baba.mobile.domain.model.Profile
import kotlinx.coroutines.flow.Flow

interface MyPageRemoteDataSource {

    suspend fun loadMyPageGroup(): Flow<GroupResponse>

    suspend fun loadBabyProfile(babyId: String): Flow<BabyProfileResponse>

    suspend fun addGroup(myPageGroup: MyPageGroup)

    suspend fun editProfile(profile: Profile)

    suspend fun editBabyName(babyId: String, name: String)

    suspend fun addMyBaby(baby: MyBaby)

    suspend fun addBabyWithInviteCode(inviteCode: InviteCode)

    suspend fun deleteBaby(babyId: String)

    suspend fun patchGroup(groupName: String, group: GroupInfo)

    suspend fun deleteGroup(groupName: String)

    suspend fun patchMember(memberId: String, relation: GroupMemberInfo)

    suspend fun deleteGroupMember(memberId: String)
}