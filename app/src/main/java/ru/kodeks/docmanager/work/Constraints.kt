package ru.kodeks.docmanager.work

import androidx.work.Constraints
import androidx.work.NetworkType

val SYNC_REQUEST_CONSTRAINTS =
    Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .build()

val PARSER_CONSTRAINTS =
    Constraints.Builder()
        .setRequiresStorageNotLow(true)
        .build()