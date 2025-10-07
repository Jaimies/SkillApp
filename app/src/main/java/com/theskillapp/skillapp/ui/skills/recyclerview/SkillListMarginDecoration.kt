package com.theskillapp.skillapp.ui.skills.recyclerview

import com.theskillapp.skillapp.shared.Dimension.Companion.dp
import com.theskillapp.skillapp.shared.recyclerview.itemdecoration.margin.BottomMarginItemDecoration

class SkillListMarginDecoration : BottomMarginItemDecoration() {
    override val marginByItemViewType = mapOf(
        SkillListAdapter.ItemType.Stopwatch to 20.dp,
        SkillListAdapter.ItemType.Skill to 25.dp,
        SkillListAdapter.ItemType.SkillGroupHeader to 20.dp,
        SkillListAdapter.ItemType.SkillGroupFooter to 24.dp,
    )
}
