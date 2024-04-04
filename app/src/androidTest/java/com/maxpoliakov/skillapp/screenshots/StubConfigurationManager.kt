package com.maxpoliakov.skillapp.screenshots

import com.maxpoliakov.skillapp.data.backup.BackupConfigurationManager
import com.maxpoliakov.skillapp.data.backup.BackupConfigurationManager.Configuration
import com.maxpoliakov.skillapp.domain.model.GenericUri
import com.maxpoliakov.skillapp.domain.repository.BackupRepository.Result
import kotlinx.coroutines.flow.flowOf

class StubConfigurationManager(
    configuration: Configuration = Configuration.Success(GenericUri("uri")),
): BackupConfigurationManager {
    override val configuration = flowOf(configuration)
    override fun handleException(throwable: Throwable) = Result.Failure.Error(throwable)
}
