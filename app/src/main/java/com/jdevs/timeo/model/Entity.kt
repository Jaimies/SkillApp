package com.jdevs.timeo.model

interface Entity<DB, Firestore> {

    fun toDB(): DB
    fun toFirestore(): Firestore
}
