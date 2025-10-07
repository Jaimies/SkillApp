package com.theskillapp.skillapp.data.extensions

import android.net.Uri
import com.theskillapp.skillapp.domain.model.GenericUri

fun GenericUri.toAndroidUri() : Uri = Uri.parse(this.uriString)
fun Uri.toGenericUri() = GenericUri(this.toString())
