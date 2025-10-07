package com.theskillapp.skillapp.domain.model

data class GenericUri(
    val uriString: String,
) {
    override fun toString() = uriString
}
