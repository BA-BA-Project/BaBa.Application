package kids.baba.mobile.domain.usecase

import kids.baba.mobile.data.repository.AlbumRepositoryImpl
import kids.baba.mobile.domain.model.Article
import kids.baba.mobile.domain.repository.AlbumRepository
import javax.inject.Inject

class PostOneArticleUseCase @Inject constructor(private val repository: AlbumRepository) {
    suspend fun post(id: String, article: Article) = repository.addArticle(id, article)
}