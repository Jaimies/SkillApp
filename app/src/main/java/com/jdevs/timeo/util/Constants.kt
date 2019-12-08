package com.jdevs.timeo.util

const val TAG = "Timeo"

object ActivitiesConstants {

    const val COLLECTION = "activities"
    const val NAME_PROPERTY = "name"
    const val ACTIVITY_ID_PROPERTY = "activityId"
    const val TOTAL_TIME_PROPERTY = "totalTime"
    const val TIMESTAMP_PROPERTY = "timestamp"

    const val FETCH_LIMIT: Long = 12
    const val VISIBLE_THRESHOLD = 3
    const val NAME_MAX_LENGTH = 100
    const val ICON_MAX_LENGTH = 100
}

object RecordsConstants {

    const val COLLECTION = "records"
    const val TIMESTAMP_PROPERTY = "timestamp"

    const val FETCH_LIMIT: Long = 20
    const val VISIBLE_THRESHOLD = 2
    const val RECORD_MIN_TIME = 5
}

object OperationConstants {

    const val ADDED = 0
    const val MODIFIED = 1
    const val REMOVED = 2
    const val FINISHED = 3
}

object RequestCodes {

    const val RC_SIGN_IN = 0
}

object AdapterConstants {

    const val LOADING = 0
    const val ACTIVITY = 1
    const val RECORD = 2
}

object Time {

    const val HOUR_MINUTES = 60
    const val DAY_HOURS = 24
}

object ViewConstants {

    const val HAS_TEXT_WATCHER = "HAS_TEXT_WATCHER"
}

object UserConstants {

    const val USERS_COLLECTION = "users"
}
