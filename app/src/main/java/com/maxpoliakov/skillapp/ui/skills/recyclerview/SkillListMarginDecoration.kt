package com.maxpoliakov.skillapp.ui.skills.recyclerview

import com.maxpoliakov.skillapp.ui.common.recyclerview.itemdecoration.margin.BottomMarginItemDecoration
import com.maxpoliakov.skillapp.util.ui.dp

class SkillListMarginDecoration : BottomMarginItemDecoration() {
    override val marginByItemViewType = mapOf(
        SkillListAdapter.ItemType.Stopwatch to 20.dp,
        SkillListAdapter.ItemType.Skill to 25.dp,
        SkillListAdapter.ItemType.SkillGroupHeader to 10.dp,
        SkillListAdapter.ItemType.SkillGroupFooter to 24.dp,
    )
}
