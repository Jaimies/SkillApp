package com.maxpoliakov.skillapp.domain.repository

import java.time.LocalDateTime

interface PremiumUtil {
    fun getFreePremiumExpiryDate(): LocalDateTime?
    fun enableFreePremium()
    fun isFreePremiumAvailable() : Boolean
}
