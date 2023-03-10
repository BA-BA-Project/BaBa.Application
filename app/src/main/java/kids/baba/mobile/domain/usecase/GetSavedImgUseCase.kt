//package kids.baba.mobile.domain.usecase
//
//import kids.baba.mobile.domain.repository.SavedImgRepository
//import kotlinx.coroutines.flow.flow
//import javax.inject.Inject
//
//class GetSavedImgUseCase @Inject constructor(
//    private val savedImgRepository: SavedImgRepository
//){
//
//    suspend fun getMe() = flow{
//        savedImgRepository.getMe()
//    }
//
//}