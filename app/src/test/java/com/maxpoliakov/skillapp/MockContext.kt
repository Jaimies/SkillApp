package com.maxpoliakov.skillapp

import android.content.Context
import com.maxpoliakov.skillapp.test.any
import org.mockito.Mockito.`when`
import org.mockito.Mockito.eq
import org.mockito.Mockito.mock

fun createMockContext(): Context {
    val context = mock(Context::class.java)
    `when`(context.getString(eq(R.string.time_minutes), any())).thenAnswer { "${it.arguments[1]}m" }
    `when`(context.getString(eq(R.string.time_hours), any())).thenAnswer { "${it.arguments[1]}h" }
    return context
}
