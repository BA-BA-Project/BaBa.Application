package kids.baba.mobile.presentation.mapper

import kids.baba.mobile.domain.model.Group
import kids.baba.mobile.presentation.model.GroupUiModel

fun List<Group>.toUiModel() : List<GroupUiModel> {
    val ownerFamilyName = this.first{ it.family}.groupName

    return this.map { group ->
        GroupUiModel(
            ownerFamily = ownerFamilyName,
            groupName = group.groupName,
            family = group.family,
            members = group.members
        )
    }

}