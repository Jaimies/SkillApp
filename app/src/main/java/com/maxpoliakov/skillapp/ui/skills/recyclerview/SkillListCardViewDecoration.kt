package com.maxpoliakov.skillapp.ui.skills.recyclerview

import com.maxpoliakov.skillapp.ui.common.recyclerview.itemdecoration.fakecardview.CardViewDecoration

class SkillListCardViewDecoration : CardViewDecoration() {
    override val cardFooterViewType get() = SkillListAdapter.ItemType.SkillGroupFooter
}
