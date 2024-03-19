package com.maxpoliakov.skillapp.data.extensions

import android.net.Uri
import com.maxpoliakov.skillapp.domain.model.GenericUri

fun GenericUri.toAndroidUri() : Uri = Uri.parse(this.uriString)
fun Uri.toGenericUri() = GenericUri(this.toString())
