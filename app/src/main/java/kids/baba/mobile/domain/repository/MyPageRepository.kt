package kids.baba.mobile.domain.repository

import kids.baba.mobile.domain.model.*
import kotlinx.coroutines.flow.Flow

interface MyPageRepository {
    suspend fun loadMyPageGroup(): Flow<GroupResponse>

    suspend fun loadBabyProfile(babyId: String): Flow<BabyProfileResponse>

    suspend fun addGroup(myPageGroup: MyPageGroup): Result<Unit>

    suspend fun editProfile(profile: Profile): Result<Unit>

    suspend fun editBabyName(babyId: String, name: String): Result<Unit>

    suspend fun addMyBaby(baby: MyBaby): Result<Unit>

    suspend fun addBabyWithInviteCode(inviteCode: InviteCode)

    suspend fun deleteBaby(babyId: String)

    suspend fun patchGroup(groupName: String, group: GroupInfo): Result<Unit>

    suspend fun deleteGroup(groupName: String): Result<Unit>

    suspend fun patchMember(memberId: String, relation: GroupMemberInfo): Result<Unit>

    suspend fun deleteGroupMember(memberId: String)
}