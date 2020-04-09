package ru.kodeks.docmanager.network.operations.signaturestamp

import ru.kodeks.docmanager.di.const.STAMP_DIR
import ru.kodeks.docmanager.model.io.GetSignatureStampRequest
import ru.kodeks.docmanager.network.api.GetSignatureStampApi
import ru.kodeks.docmanager.network.operations.base.WriteResponseToFileOperation
import javax.inject.Inject
import javax.inject.Named

class GetSignatureStampOperation @Inject constructor(
    override val api: GetSignatureStampApi,
    @Named(STAMP_DIR)
    override val responseDirectory: String
) : WriteResponseToFileOperation<GetSignatureStampApi, GetSignatureStampRequest>(
    api, responseDirectory
) {

    override lateinit var request: GetSignatureStampRequest

    override fun outputFileName() = "${request.signatureType}.png"

}