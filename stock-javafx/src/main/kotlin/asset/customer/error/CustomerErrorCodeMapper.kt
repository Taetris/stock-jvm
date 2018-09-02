package asset.customer.error

import application.usecase.ErrorCode
import application.usecase.ErrorCodeMapper

object CustomerErrorCodeMapper : ErrorCodeMapper() {

    override fun mapErrorCodeToMessage(errorCode: ErrorCode): String {
        return when (errorCode) {
            ErrorCode.ALREADY_EXISTS -> format(errorCode, bundle.getString("customerAlreadyExistsError"))
            ErrorCode . NOT_EXIST -> format(errorCode, bundle.getString("customerDoesNotExistError"))
            else -> {
                return super.mapErrorCodeToMessage(errorCode)
            }
        }
    }
}