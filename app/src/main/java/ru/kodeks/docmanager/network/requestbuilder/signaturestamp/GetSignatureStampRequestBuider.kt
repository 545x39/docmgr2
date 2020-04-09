package ru.kodeks.docmanager.network.requestbuilder.signaturestamp

import ru.kodeks.docmanager.const.SignatureType.QUALIFIED
import ru.kodeks.docmanager.const.SignatureType.SIMPLE
import ru.kodeks.docmanager.model.io.GetSignatureStampRequest
import ru.kodeks.docmanager.network.requestbuilder.RequestBuilder

abstract class GetSignatureStampRequestBuider : RequestBuilder<GetSignatureStampRequest>() {
    abstract val type: Int
    override fun request(): GetSignatureStampRequest {
        return GetSignatureStampRequest().apply {
            signatureType = type
        }
    }
}

abstract class GetSimpleSignatureStampRequestBuilder() : GetSignatureStampRequestBuider() {
    override val type: Int = SIMPLE
}

abstract class GetQualifiedSignatureStampRequestBuilder() : GetSignatureStampRequestBuider() {
    override val type: Int = QUALIFIED
}