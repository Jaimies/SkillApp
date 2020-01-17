package com.jdevs.timeo.util

const val TAG = "Timeo"

object RoomConstants {

    const val DATABASE_NAME = "timeo"
}

object PagingConstants {

    const val ACTIVITIES_PAGE_SIZE = 20
    const val PROJECTS_PAGE_SIZE = 20
    const val RECORDS_PAGE_SIZE = 50
    const val STATS_PAGE_SIZE = 40
}

object FirestoreConstants {

    const val TIMESTAMP = "timestamp"
    const val NAME = "name"
    const val ACTIVITY_ID = "activityId"
    const val TOTAL_TIME = "totalTime"
    const val RECENT_RECORDS = "recentRecords"
    const val TIME = "time"
}

object ActivitiesConstants {

    const val COLLECTION = "activities"
    const val ACTIVITIES_FIRESTORE_PAGE_SIZE = 20L
    const val VISIBLE_THRESHOLD = 5
    const val NAME_MAX_LENGTH = 100
    const val TOP_ACTIVITIES_COUNT = 3L
}

object RecordsConstants {

    const val COLLECTION = "records"
    const val FIRESTORE_RECORDS_PAGE_SIZE = 50L
    const val VISIBLE_THRESHOLD = 12
    const val RECORD_MIN_TIME = 5
}

object ProjectsConstants {

    const val COLLECTION = "projects"
    const val FIRESTORE_PROJECTS_PAGE_SIZE = 40L
    const val VISIBLE_THRESHOLD = 10
    const val TOP_PROJECTS_COUNT = 3L
}

object StatsConstants {

    const val DAY_STATS_COLLECTION = "dayStats"
    const val WEEK_STATS_COLLECTION = "weekStats"
    const val MONTH_STATS_COLLECTION = "monthStats"
    const val DAY_PROPERTY = "day"
    const val FIRESTORE_STATS_PAGE_SIZE = 30L
    const val STATS_TYPES_COUNT = 3
    const val VISIBLE_THRESHOLD = 8
}

object StatsTypes {

    const val DAY = 0
    const val WEEK = 1
    const val MONTH = 2
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

object ViewTypes {

    const val LOADING = 0
    const val ACTIVITY = 1
    const val RECORD = 2
    const val PROJECT = 3
    const val STATISTIC = 4
}
