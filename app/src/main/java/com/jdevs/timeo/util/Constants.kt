package com.jdevs.timeo.util

const val TAG = "Timeo"
const val USERS_COLLECTION = "users"

object ActivitiesConstants {
    const val COLLECTION = "activities"
    const val FETCH_LIMIT: Long = 12
    const val NAME_PROPERTY = "name"
    const val ACTIVITY_ID_PROPERTY = "activityId"
    const val TOTAL_TIME_PROPERTY = "totalTime"
    const val TIMESTAMP_PROPERTY = "timestamp"
    const val NAME_MAX_LENGTH = 100
    const val ICON_MAX_LENGTH = 100
}

object RecordsConstants {
    const val COLLECTION = "records"
    const val FETCH_LIMIT: Long = 20
    const val TIMESTAMP_PROPERTY = "timestamp"
    const val RECORD_MIN_TIME = 5
}

object AuthConstants {
    const val PASSWORD_MAX_LENGTH = 25
    const val PASSWORD_MIN_LENGTH = 6
}

object RequestCodes {
    const val RC_SIGN_IN = 1
}

object AdapterConstants {
    const val LOADING = 1
    const val ACTIVITY = 2
    const val RECORD = 3
}

object Time {
    const val HOUR_MINUTES = 60
    const val DAY_HOURS = 24
}

object StringConstants {
    const val FIRST_RANDOM_SYMBOL_CODE = 32
    const val LAST_RANDOM_SYMBOL_CODE = 127
}
