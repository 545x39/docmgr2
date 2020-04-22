package ru.kodeks.docmanager.work

import androidx.work.*
import ru.kodeks.docmanager.const.PathsAndFileNames
import ru.kodeks.docmanager.work.checkstate.CheckStateWorker
import ru.kodeks.docmanager.work.signaturestamps.GetQualifiedSignatureStampWorker
import ru.kodeks.docmanager.work.signaturestamps.GetSimpleSignatureStampWorker
import ru.kodeks.docmanager.work.sync.ParserWorker
import ru.kodeks.docmanager.work.sync.SyncWorker
import ru.kodeks.docmanager.work.sync.TestWorker
import java.util.concurrent.TimeUnit

const val SYNC_TAG = "syncTag"
const val DOWNLOAD_STAMP_TAG = "syncTag"
const val TEST_TAG = "testTag"

private fun credentials(login: String, password: String): Data {
    return workDataOf(PathsAndFileNames.LOGIN to login, PathsAndFileNames.PASSWORD to password)
}

fun testWorker(data : Data): OneTimeWorkRequest {
    return OneTimeWorkRequestBuilder<TestWorker>()
        .setInputData(data)
//        .setConstraints(SYNC_REQUEST_CONSTRAINTS)
            .addTag(TEST_TAG)
//        .setBackoffCriteria(
//            BackoffPolicy.EXPONENTIAL,
//            OneTimeWorkRequest.DEFAULT_BACKOFF_DELAY_MILLIS,
//            TimeUnit.MILLISECONDS)
        .build()
}

fun syncWorker(data : Data): OneTimeWorkRequest {
    return OneTimeWorkRequestBuilder<SyncWorker>()
        .setInputData(data)
        .setConstraints(SYNC_REQUEST_CONSTRAINTS)
        .addTag(SYNC_TAG)
        .setBackoffCriteria(
            BackoffPolicy.EXPONENTIAL,
            OneTimeWorkRequest.DEFAULT_BACKOFF_DELAY_MILLIS,
            TimeUnit.MILLISECONDS
        ).build()
}

fun parserWorker(data : Data): OneTimeWorkRequest {
    return OneTimeWorkRequestBuilder<ParserWorker>()
        .setInputData(data)
        .setConstraints(PARSER_CONSTRAINTS)
        .addTag(SYNC_TAG)
        .setBackoffCriteria(
            BackoffPolicy.EXPONENTIAL,
            OneTimeWorkRequest.DEFAULT_BACKOFF_DELAY_MILLIS,
            TimeUnit.MILLISECONDS
        ).build()
}

fun checkStateWorker() = OneTimeWorkRequestBuilder<CheckStateWorker>()
    .setConstraints(SYNC_REQUEST_CONSTRAINTS)
    .addTag(SYNC_TAG)
    .setBackoffCriteria(
        BackoffPolicy.EXPONENTIAL,
        OneTimeWorkRequest.DEFAULT_BACKOFF_DELAY_MILLIS,
        TimeUnit.MILLISECONDS
    ).build()

fun getSimpleSignatureStampWorker(data : Data) =
    OneTimeWorkRequestBuilder<GetSimpleSignatureStampWorker>()
        .setInputData(data)
        .setConstraints(SYNC_REQUEST_CONSTRAINTS)
        .addTag(DOWNLOAD_STAMP_TAG)
        .setBackoffCriteria(
            BackoffPolicy.EXPONENTIAL,
            OneTimeWorkRequest.DEFAULT_BACKOFF_DELAY_MILLIS,
            TimeUnit.MILLISECONDS
        ).build()

fun getQualifiedSignatureStampWorker(data : Data) =
    OneTimeWorkRequestBuilder<GetQualifiedSignatureStampWorker>()
        .setInputData(data)
        .setConstraints(SYNC_REQUEST_CONSTRAINTS)
        .addTag(DOWNLOAD_STAMP_TAG)
        .setBackoffCriteria(
            BackoffPolicy.EXPONENTIAL,
            OneTimeWorkRequest.DEFAULT_BACKOFF_DELAY_MILLIS,
            TimeUnit.MILLISECONDS
        ).build()