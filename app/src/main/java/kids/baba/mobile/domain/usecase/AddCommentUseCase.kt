package kids.baba.mobile.domain.usecase

import kids.baba.mobile.domain.model.Comment
import kids.baba.mobile.domain.repository.AlbumRepository
import javax.inject.Inject

class AddCommentUseCase @Inject constructor(private val repository: AlbumRepository) {
    suspend fun add(id: String, contentId: String, comment: Comment) =
        repository.addComment(id, contentId, comment)
}