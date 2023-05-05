package kids.baba.mobile.presentation.mapper

import kids.baba.mobile.domain.model.Album
import kids.baba.mobile.presentation.model.AlbumUiModel
import kids.baba.mobile.presentation.model.RepresentativeAlbumUiModel

fun Album.toPresentation(isMyBaby: Boolean) = AlbumUiModel(contentId, ownerName, relation, date, title, like, photo, cardStyle, isMyBaby)

fun AlbumUiModel.toPresentation() = RepresentativeAlbumUiModel(photo, date)