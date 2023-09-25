package kids.baba.mobile.domain.repository

import kids.baba.mobile.domain.model.*

interface MyPageRepository {
    suspend fun loadMyPageGroup(): ApiResult<GroupResponse>

    suspend fun loadBabyProfile(babyId: String): ApiResult<BabyProfileResponse>

    suspend fun addGroup(myPageGroup: MyPageGroup): ApiResult<Unit>

    suspend fun editProfile(profile: Profile): ApiResult<Unit>

    suspend fun editBabyName(babyId: String, name: String): ApiResult<Unit>

    suspend fun addMyBaby(baby: MyBaby): ApiResult<Unit>

    suspend fun addBabyWithInviteCode(inviteCode: InviteCode): ApiResult<Unit>

    suspend fun deleteBaby(babyId: String): ApiResult<Unit>

    suspend fun patchGroup(groupName: String, group: GroupInfo): ApiResult<Unit>

    suspend fun deleteGroup(groupName: String): ApiResult<Unit>

    suspend fun patchMember(memberId: String, relation: GroupMemberInfo): ApiResult<Unit>

    suspend fun deleteGroupMember(memberId: String): ApiResult<Unit>

    suspend fun getInvitationInfo(inviteCode: String): ApiResult<BabiesInfoResponse>

    suspend fun makeInviteCode(relationInfo: RelationInfo): ApiResult<InviteCode>
}