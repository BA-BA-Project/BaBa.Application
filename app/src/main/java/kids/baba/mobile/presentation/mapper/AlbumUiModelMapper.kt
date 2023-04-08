package kids.baba.mobile.presentation.mapper

import kids.baba.mobile.domain.model.Album
import kids.baba.mobile.presentation.model.AlbumUiModel

fun Album.toPresentation() = AlbumUiModel(contentId, ownerName, relation, date, title, like, photo, cardStyle)