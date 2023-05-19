package kids.baba.mobile.data.datasource.mypage

import kids.baba.mobile.data.api.MyPageApi
import kids.baba.mobile.data.network.SafeApiHelper
import kids.baba.mobile.domain.model.BabyEdit
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
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MyPageRemoteDataSourceImpl @Inject constructor(
    private val api: MyPageApi,
    private val safeApiHelper: SafeApiHelper
) : MyPageRemoteDataSource {
    override suspend fun loadMyPageGroup(): Flow<GroupResponse> {
        val result = safeApiHelper.getSafe(
            remoteFetch = {
                api.loadMyPageGroup()
            },
            mapping = {
                it
            }
        )
        return if (result is Result.Success) flow { emit(result.data) } else flow {}
    }

    override suspend fun loadBabyProfile(babyId: String): Flow<BabyProfileResponse> {
        val result = safeApiHelper.getSafe(
            remoteFetch = {
                api.loadBabyProfile(babyId = babyId)
            },
            mapping = {
                it
            }
        )
        return if (result is Result.Success) flow { emit(result.data) } else flow {}
    }

    override suspend fun addGroup(myPageGroup: MyPageGroup) {
        api.addGroup(myPageGroup = myPageGroup)
    }

    override suspend fun editProfile(profile: Profile): Result<Unit> {
        val result = safeApiHelper.getSafe(
            remoteFetch = {
                api.editProfile(profile = profile)
            },
            mapping = {}
        )
        return if (result is Result.Failure) {
            Result.Failure(result.code, result.message, Exception(result.message))
        } else {
            result
        }
    }

    override suspend fun editBabyName(babyId: String, name: String) {
        api.editBabyName(babyId = babyId, babyEdit = BabyEdit(name))
    }

    override suspend fun addMyBaby(baby: MyBaby): Result<Unit> {
        val result = safeApiHelper.getSafe(
            remoteFetch = {
                api.addMyBaby(baby = baby)
            },
            mapping = {}
        )
        return if (result is Result.Failure) {
            Result.Failure(result.code, result.message, Exception(result.message))
        } else {
            result
        }
    }

    override suspend fun addBabyWithInviteCode(inviteCode: InviteCode) {
        api.addBabyWithInviteCode(inviteCode = inviteCode)
    }

    override suspend fun deleteBaby(babyId: String) {
        api.deleteBaby(babyId = babyId)
    }

    override suspend fun patchGroup(groupName: String, group: GroupInfo) {
        api.patchGroup(groupName = groupName, group = group)
    }

    override suspend fun deleteGroup(groupName: String) {
        api.deleteGroup(groupName = groupName)
    }

    override suspend fun patchMember(memberId: String, relation: GroupMemberInfo) {
        api.patchMember(memberId = memberId, relationName = relation)
    }

    override suspend fun deleteGroupMember(memberId: String) {
        api.deleteGroupMember(memberId = memberId)
    }
}