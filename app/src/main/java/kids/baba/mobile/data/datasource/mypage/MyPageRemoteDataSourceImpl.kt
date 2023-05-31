package kids.baba.mobile.data.datasource.mypage

import android.util.Log
import kids.baba.mobile.data.api.MyPageApi
import kids.baba.mobile.data.network.SafeApiHelper
import kids.baba.mobile.domain.model.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MyPageRemoteDataSourceImpl @Inject constructor(
    private val api: MyPageApi,
    private val safeApiHelper: SafeApiHelper
) : MyPageRemoteDataSource {
    override suspend fun loadMyPageGroup(): Result<GroupResponse> {
        val result = safeApiHelper.getSafe(
            remoteFetch = {
                api.loadMyPageGroup()
            },
            mapping = {
                it
            }
        )
        return if (result is Result.Failure) {
            Result.Failure(result.code, result.message, Exception(result.message))
        } else {
            result
        }
//        return if (result is Result.Success) flow { emit(result.data) } else flow {}
    }

    override suspend fun loadBabyProfile(babyId: String): Result<BabyProfileResponse> {
        val result = safeApiHelper.getSafe(
            remoteFetch = {
                api.loadBabyProfile(babyId = babyId)
            },
            mapping = {
                it
            }
        )

        return if (result is Result.Failure) {
            Result.Failure(result.code, result.message, Exception(result.message))
        } else {
            result
        }
    }

    override suspend fun addGroup(myPageGroup: MyPageGroup): Result<Unit> {
        val result = safeApiHelper.getSafe(
            remoteFetch = {
                api.addGroup(myPageGroup = myPageGroup)
            },
            mapping = {}
        )
        return if (result is Result.Failure) {
            Result.Failure(result.code, result.message, Exception(result.message))
        } else {
            result
        }
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

    override suspend fun editBabyName(babyId: String, name: String): Result<Unit> {
        val result = safeApiHelper.getSafe(
            remoteFetch = {
                api.editBabyName(babyId = babyId, babyEdit = BabyEdit(name))
            },
            mapping = {}
        )
        return if (result is Result.Failure) {
            Result.Failure(result.code, result.message, Exception(result.message))
        } else {
            result
        }
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

    override suspend fun addBabyWithInviteCode(inviteCode: InviteCode): Result<Unit> {
        val result = safeApiHelper.getSafe(
            remoteFetch = {
                api.addBabyWithInviteCode(inviteCode = inviteCode)
            },
            mapping = {}
        )
        return if (result is Result.Failure) {
            Result.Failure(result.code, result.message, Exception(result.message))
        } else {
            result
        }
    }

    override suspend fun deleteBaby(babyId: String) {
        api.deleteBaby(babyId = babyId)
    }

    override suspend fun patchGroup(groupName: String, group: GroupInfo): Result<Unit> {
        val result = safeApiHelper.getSafe(
            remoteFetch = {
                api.patchGroup(groupName = groupName, group = group)
            },
            mapping = {}
        )
        return if (result is Result.Failure) {
            Result.Failure(result.code, result.message, Exception(result.message))
        } else {
            result
        }
    }

    override suspend fun deleteGroup(groupName: String): Result<Unit> {
        val result = safeApiHelper.getSafe(
            remoteFetch = {
                api.deleteGroup(groupName = groupName)
            },
            mapping = {}
        )
        return if (result is Result.Failure) {
            Result.Failure(result.code, result.message, Exception(result.message))
        } else {
            result
        }
    }

    override suspend fun patchMember(memberId: String, relation: GroupMemberInfo): Result<Unit> {
        val result = safeApiHelper.getSafe(
            remoteFetch = {
                api.patchMember(memberId = memberId, relationName = relation)
            },
            mapping = {}
        )
        return if (result is Result.Failure) {
            Result.Failure(result.code, result.message, Exception(result.message))
        } else {
            result
        }
    }

    override suspend fun deleteGroupMember(memberId: String): Result<Unit> {
        val result = safeApiHelper.getSafe(
            remoteFetch = {
                api.deleteGroupMember(memberId = memberId)
            },
            mapping = {}
        )
        return if (result is Result.Failure) {
            Result.Failure(result.code, result.message, Exception(result.message))
        } else {
            result
        }
    }

    override suspend fun getInvitationInfo(inviteCode: String): Result<BabiesInfoResponse> {
        val result = safeApiHelper.getSafe(
            remoteFetch = {
                api.getInvitationInfo(inviteCode = inviteCode)
            },
            mapping = {
                it
            }
        )
        return if (result is Result.Failure) {
            Result.Failure(result.code, result.message, Exception(result.message))
        } else {
            result
        }
    }

    override suspend fun makeInviteCode(relationInfo: RelationInfo): Result<InviteCode> {
        val result = safeApiHelper.getSafe(
            remoteFetch = {
                api.makeInviteCode(relationInfo = relationInfo)
            },
            mapping = {
                it
            }
        )
        return if (result is Result.Failure) {
            Result.Failure(result.code, result.message, Exception(result.message))
        } else {
            result
        }
    }

}