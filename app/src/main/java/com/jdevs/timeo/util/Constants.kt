package com.jdevs.timeo.util

const val TAG = "Timeo"

object RoomConstants {

    const val DATABASE_NAME = "timeo"
}

object PagingConstants {

    const val ACTIVITIES_PAGE_SIZE = 20
    const val RECORDS_PAGE_SIZE = 50
    const val STATS_PAGE_SIZE = 40
}

object FirestoreConstants {

    const val TIMESTAMP_PROPERTY = "timestamp"
    const val NAME_PROPERTY = "name"
    const val ACTIVITY_ID_PROPERTY = "activityId"
    const val TOTAL_TIME_PROPERTY = "totalTime"
}

object ActivitiesConstants {

    const val COLLECTION = "activities"
    const val PAGE_SIZE = 20L
    const val VISIBLE_THRESHOLD = 5
    const val NAME_MAX_LENGTH = 100
}

object RecordsConstants {

    const val COLLECTION = "records"
    const val PAGE_SIZE = 50L
    const val VISIBLE_THRESHOLD = 12
    const val RECORD_MIN_TIME = 5
}

object StatsConstants {

    const val VISIBLE_THRESHOLD = 10
}

object GraphTypes {

    const val DAY = 0
    const val WEEK = 1
}

object OperationTypes {

    const val SUCCESSFUL = 0
    const val FAILED = 1
    const val ADDED = 2
    const val MODIFIED = 3
    const val REMOVED = 4
    const val LAST_ITEM_REACHED = 5
}

object RequestCodes {

    const val RC_SIGN_IN = 0
}

object AdapterConstants {

    const val LOADING = 0
    const val ACTIVITY = 1
    const val RECORD = 2
    const val STATISTIC = 3
}

object Time {

    const val HOUR_MINUTES = 60
    const val DAY_HOURS = 24
    const val WEEK_DAYS = 7
}

object ViewConstants {

    const val HAS_TEXT_WATCHER = "HAS_TEXT_WATCHER"
}
