package com.maxpoliakov.skillapp.ui.skills.recyclerview

import com.maxpoliakov.skillapp.ui.common.cardviewdecoration.CardViewDecoration

class SkillListCardViewDecoration : CardViewDecoration() {
    override val cardFooterViewType get() = SkillListAdapter.ItemType.SkillGroupFooter
}
