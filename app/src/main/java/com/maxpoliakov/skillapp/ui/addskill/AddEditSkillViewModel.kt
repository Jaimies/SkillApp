package com.maxpoliakov.skillapp.ui.addskill

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.domain.model.Skill
import com.maxpoliakov.skillapp.domain.usecase.skill.AddSkillUseCase
import com.maxpoliakov.skillapp.domain.usecase.skill.DeleteSkillUseCase
import com.maxpoliakov.skillapp.domain.usecase.skill.SaveSkillUseCase
import com.maxpoliakov.skillapp.lifecycle.SingleLiveEvent
import com.maxpoliakov.skillapp.model.SkillItem
import com.maxpoliakov.skillapp.model.mapToDomain
import com.maxpoliakov.skillapp.ui.common.viewmodel.KeyboardHidingViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.time.Duration
import javax.inject.Inject

private const val NAME_MAX_LENGTH = 100

class AddEditSkillViewModel(
    private val addSkill: AddSkillUseCase,
    private val saveSkill: SaveSkillUseCase,
    private val deleteSkill: DeleteSkillUseCase,
    private val ioScope: CoroutineScope,
    private val skill: SkillItem?
) : KeyboardHidingViewModel() {

    val name = MutableLiveData(skill?.name)
    val totalTime = MutableLiveData<String>()
    val nameError = MutableLiveData(-1)
    val showDeleteDialog = SingleLiveEvent<Any>()
    val skillExists = skill != null

    val navigateBack: LiveData<Any> get() = _navigateBack
    private val _navigateBack = SingleLiveEvent<Any>()

    fun deleteSkill() = ioScope.launch {
        skill?.let {
            deleteSkill.run(skill.mapToDomain())
        }
    }

    fun saveSkill() {

        val name = name.value.orEmpty()
        if (!validateName(name)) return

        ioScope.launch {

            if (skill != null) {
                saveSkill.run(skill.mapToDomain().copy(name = name))
            } else {
                addSkill.run(
                    Skill(
                        name = name,
                        totalTime = Duration.ofHours(totalTime.value?.toLong() ?: 0)
                    )
                )
            }
        }

        _navigateBack.call()
    }

    private fun validateName(name: String): Boolean {
        when {
            name.isEmpty() -> nameError.value = R.string.name_empty
            name.length >= NAME_MAX_LENGTH -> nameError.value =
                R.string.name_too_long
            else -> return true
        }

        return false
    }

    fun showDeleteDialog() = showDeleteDialog.call()

    class Factory @Inject constructor(
        private val addSkill: AddSkillUseCase,
        private val saveSkill: SaveSkillUseCase,
        private val deleteSkill: DeleteSkillUseCase,
        private val ioScope: CoroutineScope
    ) {
        fun create(skill: SkillItem?) = AddEditSkillViewModel(
            addSkill, saveSkill,
            deleteSkill, ioScope, skill
        )
    }
}
