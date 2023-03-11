package com.maxpoliakov.skillapp.ui.skills.recyclerview

import com.maxpoliakov.skillapp.ui.common.recyclerview.itemdecoration.fakecardview.FakeCardViewDecoration

class SkillListCardViewDecoration : FakeCardViewDecoration() {
    override val cardFooterViewType get() = SkillListAdapter.ItemType.SkillGroupFooter
}
