package com.maxpoliakov.skillapp.ui.skills.recyclerview

import com.maxpoliakov.skillapp.shared.recyclerview.itemdecoration.fakecardview.FakeCardViewDecoration

class SkillListCardViewDecoration : FakeCardViewDecoration() {
    override val cardFooterViewType get() = SkillListAdapter.ItemType.SkillGroupFooter
}
