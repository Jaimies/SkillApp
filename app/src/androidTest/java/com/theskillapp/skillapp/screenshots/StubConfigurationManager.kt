package com.theskillapp.skillapp.screenshots

import com.theskillapp.skillapp.data.backup.BackupConfigurationManager
import com.theskillapp.skillapp.data.backup.BackupConfigurationManager.Configuration
import com.theskillapp.skillapp.domain.model.GenericUri
import com.theskillapp.skillapp.domain.repository.BackupRepository.Result
import kotlinx.coroutines.flow.flowOf

class StubConfigurationManager(
    configuration: Configuration = Configuration.Success(GenericUri("uri")),
): BackupConfigurationManager {
    override val configuration = flowOf(configuration)
    override fun handleException(throwable: Throwable) = Result.Failure.Error(throwable)
}
