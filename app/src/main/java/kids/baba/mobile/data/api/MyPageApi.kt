package kids.baba.mobile.data.api

import kids.baba.mobile.core.constant.PrefsKey
import kids.baba.mobile.core.utils.EncryptedPrefs
import kids.baba.mobile.domain.model.*
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface MyPageApi {

    @GET("members/my-page")
    suspend fun loadMyPageGroup(
        @Header("Authorization") token: String = EncryptedPrefs.getString(PrefsKey.ACCESS_TOKEN_KEY)
    ): Response<GroupResponse>

    @GET("members/baby-page/{babyId}")
    suspend fun loadBabyProfile(
        @Header("Authorization") token: String = EncryptedPrefs.getString(PrefsKey.ACCESS_TOKEN_KEY),
        @Path("babyId") babyId: String
    ): Response<BabyProfileResponse>

    @POST("members/groups")
    suspend fun addGroup(
        @Header("Authorization") token: String = EncryptedPrefs.getString(PrefsKey.ACCESS_TOKEN_KEY),
        @Body myPageGroup: MyPageGroup
    ): Response<Unit>

    @PUT("members")
    suspend fun editProfile(
        @Header("Authorization") token: String = EncryptedPrefs.getString(PrefsKey.ACCESS_TOKEN_KEY),
        @Body profile: Profile
    ): Response<Unit>

    @PATCH("baby/{babyId}")
    suspend fun editBabyName(
        @Header("Authorization") token: String = EncryptedPrefs.getString(PrefsKey.ACCESS_TOKEN_KEY),
        @Path("babyId") babyId: String,
        @Body babyEdit: BabyEdit
    ): Response<Unit>

    @POST("baby")
    suspend fun addMyBaby(
        @Header("Authorization") token: String = EncryptedPrefs.getString(PrefsKey.ACCESS_TOKEN_KEY),
        @Body baby: MyBaby
    ): Response<Unit>

    @POST("baby/code")
    suspend fun addBabyWithInviteCode(
        @Header("Authorization") token: String = EncryptedPrefs.getString(PrefsKey.ACCESS_TOKEN_KEY),
        @Body inviteCode: InviteCode
    ): Response<Unit>

    @DELETE("baby/{babyId}")
    suspend fun deleteBaby(
        @Header("Authorization") token: String = EncryptedPrefs.getString(PrefsKey.ACCESS_TOKEN_KEY),
        @Path("babyId") babyId: String
    )

    @PATCH("members/groups")
    suspend fun patchGroup(
        @Header("Authorization") token: String = EncryptedPrefs.getString(PrefsKey.ACCESS_TOKEN_KEY),
        @Query("groupName") groupName: String,
        @Body group: GroupInfo
    ): Response<Unit>

    @DELETE("members/groups")
    suspend fun deleteGroup(
        @Header("Authorization") token: String = EncryptedPrefs.getString(PrefsKey.ACCESS_TOKEN_KEY),
        @Query("groupName") groupName: String
    ): Response<Unit>

    @PATCH("members/groups/{groupMemberId}")
    suspend fun patchMember(
        @Header("Authorization") token: String = EncryptedPrefs.getString(PrefsKey.ACCESS_TOKEN_KEY),
        @Path("groupMemberId") memberId: String,
        @Body relationName: GroupMemberInfo
    ): Response<Unit>

    //TODO FIX
    @DELETE("members/groups/{groupMemberId}")
    suspend fun deleteGroupMember(
        @Header("Authorization") token: String = EncryptedPrefs.getString(PrefsKey.ACCESS_TOKEN_KEY),
        @Path("groupMemberId") memberId: String
    ): Response<Unit>


    @GET("baby/invitation")
    suspend fun getInvitationInfo(
        @Query("code") inviteCode: String): Response<BabiesInfoResponse>


    @POST("baby/invite-code")
    suspend fun makeInviteCode(
        @Header("Authorization") token: String = EncryptedPrefs.getString(PrefsKey.ACCESS_TOKEN_KEY),
        @Body relationInfo: RelationInfo
    ): Response<InviteCode>


}