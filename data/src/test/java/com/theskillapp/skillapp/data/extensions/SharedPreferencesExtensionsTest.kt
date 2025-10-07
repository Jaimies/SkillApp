package com.theskillapp.skillapp.data.extensions

import androidx.core.content.edit
import com.theskillapp.skillapp.data.StubSharedPreferences
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SharedPreferencesExtensionsTest: StringSpec({
    "getStringFlow()" {
        val sharedPreferences = StubSharedPreferences(mapOf("key" to "initial_value"))

        val scope = CoroutineScope(Dispatchers.Default)
        val values = mutableListOf<String?>()

        scope.launch {
            sharedPreferences
                .getStringFlow("key")
                .collect { values.add(it) }
        }

        // the delay is necessary to make sure that the coroutine has been
        // launched and the collection of values has begun
        delay(100)

        sharedPreferences.edit { putString("key", "value") }
        sharedPreferences.edit { remove("key") }
        sharedPreferences.edit { putString("key", "new_value") }

        scope.cancel()

        values shouldBe mutableListOf("initial_value", "value", null, "new_value")
    }
})
