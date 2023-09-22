package kids.baba.mobile.data.api

import kids.baba.mobile.domain.model.*
import retrofit2.Response
import retrofit2.http.*

interface MyPageApi {

    @GET("members/my-page")
    suspend fun loadMyPageGroup(
    ): Response<GroupResponse>

    @GET("members/baby-page/{babyId}")
    suspend fun loadBabyProfile(
        @Path("babyId") babyId: String
    ): Response<BabyProfileResponse>

    @POST("members/groups")
    suspend fun addGroup(
        @Body myPageGroup: MyPageGroup
    ): Response<Unit>

    @PUT("members")
    suspend fun editProfile(
        @Body profile: Profile
    ): Response<Unit>

    @PATCH("baby/{babyId}")
    suspend fun editBabyName(
        @Path("babyId") babyId: String,
        @Body babyEdit: BabyEdit
    ): Response<Unit>

    @POST("baby")
    suspend fun addMyBaby(
        @Body baby: MyBaby
    ): Response<Unit>

    @POST("baby/code")
    suspend fun addBabyWithInviteCode(
        @Body inviteCode: InviteCode
    ): Response<Unit>

    @DELETE("baby/{babyId}")
    suspend fun deleteBaby(
        @Path("babyId") babyId: String
    ): Response<Unit>

    @PATCH("members/groups")
    suspend fun patchGroup(
        @Query("groupName") groupName: String,
        @Body group: GroupInfo
    ): Response<Unit>

    @DELETE("members/groups")
    suspend fun deleteGroup(
        @Query("groupName") groupName: String
    ): Response<Unit>

    @PATCH("members/groups/{groupMemberId}")
    suspend fun patchMember(
        @Path("groupMemberId") memberId: String,
        @Body relationName: GroupMemberInfo
    ): Response<Unit>

    //TODO FIX
    @DELETE("members/groups/{groupMemberId}")
    suspend fun deleteGroupMember(
        @Path("groupMemberId") memberId: String
    ): Response<Unit>

    @GET("baby/invitation")
    suspend fun getInvitationInfo(
        @Query("code") inviteCode: String
    ): Response<BabiesInfoResponse>

    @POST("baby/invite-code")
    suspend fun makeInviteCode(
        @Body relationInfo: RelationInfo
    ): Response<InviteCode>


}