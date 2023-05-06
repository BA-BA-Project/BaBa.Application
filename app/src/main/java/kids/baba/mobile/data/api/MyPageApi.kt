package kids.baba.mobile.data.api

import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT

interface MyPageApi{

    @GET("members/my-page")
    suspend fun loadMyPageGroup()

    @GET("/members/baby-page/{babyId}")
    suspend fun loadBabyProfile()

    @POST("/members/groups")
    suspend fun addGroup()

    @PUT("/members")
    suspend fun editProfile()

    @PATCH("/baby/{babyId}")
    suspend fun editBabyName()

    @POST("/baby")
    suspend fun addMyBaby()

    @POST("/baby/code")
    suspend fun addBabyWithInviteCode()

    @DELETE("/baby/{babyId}")
    suspend fun deleteBaby()

    @PATCH("/members/groups")
    suspend fun patchGroup()

    @DELETE("/members/groups")
    suspend fun deleteGroup()

    @PATCH("/members/groups/{memberId}")
    suspend fun patchMember()

    @DELETE("/members/groups/{memberId}")
    suspend fun deleteGroupMember()

}