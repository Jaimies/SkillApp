package com.maxpoliakov.skillapp.ui.skills.recyclerview

import com.maxpoliakov.skillapp.shared.Dimension.Companion.dp
import com.maxpoliakov.skillapp.shared.recyclerview.itemdecoration.margin.BottomMarginItemDecoration

class SkillListMarginDecoration : BottomMarginItemDecoration() {
    override val marginByItemViewType = mapOf(
        SkillListAdapter.ItemType.Stopwatch to 20.dp,
        SkillListAdapter.ItemType.Skill to 25.dp,
        SkillListAdapter.ItemType.SkillGroupHeader to 20.dp,
        SkillListAdapter.ItemType.SkillGroupFooter to 24.dp,
    )
}
