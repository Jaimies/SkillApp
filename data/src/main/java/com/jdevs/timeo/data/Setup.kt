package com.jdevs.timeo.data

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.firestoreSettings
import com.google.firebase.functions.ktx.functions
import com.google.firebase.ktx.Firebase

fun setupFirebase() {
    if (!BuildConfig.DEBUG) return

    Firebase.functions("europe-west1").useFunctionsEmulator("10.0.2.2:5001")
    Firebase.firestore.firestoreSettings = firestoreSettings {
        host = "10.0.2.2:8080"
        isPersistenceEnabled = false
        isSslEnabled = false
    }
}