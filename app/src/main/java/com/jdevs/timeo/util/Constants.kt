package com.jdevs.timeo.util

const val TAG = "Timeo"

object FirestoreConstants {

    const val TIMESTAMP_PROPERTY = "timestamp"
    const val NAME_PROPERTY = "name"
    const val ACTIVITY_ID_PROPERTY = "activityId"
    const val TOTAL_TIME_PROPERTY = "totalTime"
}

object ActivitiesConstants {

    const val COLLECTION = "activities"
    const val FETCH_LIMIT = 12L
    const val VISIBLE_THRESHOLD = 3
    const val NAME_MAX_LENGTH = 100
}

object RecordsConstants {

    const val COLLECTION = "records"
    const val FETCH_LIMIT = 20L
    const val VISIBLE_THRESHOLD = 2
    const val RECORD_MIN_TIME = 5
}

object OperationTypes {

    const val SUCCESSFUL = 0
    const val FAILED = 1
    const val LAST_ITEM_REACHED = 2
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
