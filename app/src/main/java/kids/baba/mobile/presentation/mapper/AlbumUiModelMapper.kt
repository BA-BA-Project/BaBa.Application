package kids.baba.mobile.presentation.mapper

import kids.baba.mobile.domain.model.Album
import kids.baba.mobile.presentation.model.AlbumUiModel
import java.time.LocalDate

fun Album.toPresentation() = AlbumUiModel(contentId, ownerName, relation, LocalDate.now(), title, like, photo, cardStyle)