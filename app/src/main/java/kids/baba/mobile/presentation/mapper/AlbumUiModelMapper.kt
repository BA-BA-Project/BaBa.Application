package kids.baba.mobile.presentation.mapper

import kids.baba.mobile.domain.model.Album
import kids.baba.mobile.presentation.model.AlbumUiModel
import kids.baba.mobile.presentation.model.CardStyleUiModel

fun Album.toPresentation(isMyBaby: Boolean) = AlbumUiModel(contentId, ownerName, relation, date, title, like, photo, CardStyleUiModel.valueOf(cardStyle), isMyBaby)