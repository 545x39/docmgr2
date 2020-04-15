package ru.kodeks.docmanager.work

import androidx.work.*
import ru.kodeks.docmanager.const.PathsAndFileNames
import ru.kodeks.docmanager.work.checkstate.CheckStateWorker
import ru.kodeks.docmanager.work.signaturestamps.GetQualifiedSignatureStampWorker
import ru.kodeks.docmanager.work.signaturestamps.GetSimpleSignatureStampWorker
import ru.kodeks.docmanager.work.sync.SyncWorker
import java.util.concurrent.TimeUnit

interface IWorkRequestBuilder {

}

private fun credentials(login: String, password: String): Data {
    return workDataOf(PathsAndFileNames.LOGIN to login, PathsAndFileNames.PASSWORD to password)
}

fun syncWorker(login: String, password: String): OneTimeWorkRequest {
    return OneTimeWorkRequestBuilder<SyncWorker>()
        .setInputData(credentials(login, password))
        .setConstraints(SYNC_REQUEST_CONSTRAINTS)
//            .addTag(SYNC_REQUEST_TAG)
        .setBackoffCriteria(
            BackoffPolicy.EXPONENTIAL,
            OneTimeWorkRequest.DEFAULT_BACKOFF_DELAY_MILLIS,
            TimeUnit.MILLISECONDS
        ).build()
}

fun checkStateWorker() = OneTimeWorkRequestBuilder<CheckStateWorker>()
    .setConstraints(SYNC_REQUEST_CONSTRAINTS)
    .setBackoffCriteria(
        BackoffPolicy.EXPONENTIAL,
        OneTimeWorkRequest.DEFAULT_BACKOFF_DELAY_MILLIS,
        TimeUnit.MILLISECONDS
    ).build()

fun getSimpleSignatureStampWorker(login: String, password: String) =
    OneTimeWorkRequestBuilder<GetSimpleSignatureStampWorker>()
        .setInputData(credentials(login, password))
        .setConstraints(SYNC_REQUEST_CONSTRAINTS)
//                .addTag(SYNC_REQUEST_TAG)
        .setBackoffCriteria(
            BackoffPolicy.EXPONENTIAL,
            OneTimeWorkRequest.DEFAULT_BACKOFF_DELAY_MILLIS,
            TimeUnit.MILLISECONDS
        ).build()

fun getQualifiedSignatureStampWorker(login: String, password: String) =
    OneTimeWorkRequestBuilder<GetQualifiedSignatureStampWorker>()
        .setInputData(credentials(login, password))
        .setConstraints(SYNC_REQUEST_CONSTRAINTS)
//                .addTag(SYNC_REQUEST_TAG)
        .setBackoffCriteria(
            BackoffPolicy.EXPONENTIAL,
            OneTimeWorkRequest.DEFAULT_BACKOFF_DELAY_MILLIS,
            TimeUnit.MILLISECONDS
        ).build()