package com.maxpoliakov.skillapp.domain.repository

interface PremiumUtil {
    fun enableFreePremium()
    fun isFreePremiumAvailable() : Boolean
}
