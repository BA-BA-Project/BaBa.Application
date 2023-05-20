package kids.baba.mobile.domain.repository

import kids.baba.mobile.domain.model.BabyProfileResponse
import kids.baba.mobile.domain.model.GroupInfo
import kids.baba.mobile.domain.model.GroupMemberInfo
import kids.baba.mobile.domain.model.GroupResponse
import kids.baba.mobile.domain.model.InviteCode
import kids.baba.mobile.domain.model.MyBaby
import kids.baba.mobile.domain.model.MyPageGroup
import kids.baba.mobile.domain.model.Profile
import kids.baba.mobile.domain.model.Result
import kotlinx.coroutines.flow.Flow

interface MyPageRepository {
    suspend fun loadMyPageGroup(): Flow<GroupResponse>

    suspend fun loadBabyProfile(babyId: String): Flow<BabyProfileResponse>

    suspend fun addGroup(myPageGroup: MyPageGroup)

    suspend fun editProfile(profile: Profile): Result<Unit>

    suspend fun editBabyName(babyId: String, name: String): Result<Unit>

    suspend fun addMyBaby(baby: MyBaby): Result<Unit>

    suspend fun addBabyWithInviteCode(inviteCode: InviteCode)

    suspend fun deleteBaby(babyId: String)

    suspend fun patchGroup(groupName: String, group: GroupInfo)

    suspend fun deleteGroup(groupName: String)

    suspend fun patchMember(memberId: String, relation: GroupMemberInfo): Result<Unit>

    suspend fun deleteGroupMember(memberId: String)
}