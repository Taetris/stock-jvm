package asset.item.error

import application.ResourceLoader
import application.usecase.ErrorCode
import application.usecase.ErrorCodeMapper

object ItemErrorCodeMapper : ErrorCodeMapper() {

    override fun mapErrorCodeToMessage(errorCode: ErrorCode): String {
        val bundle = ResourceLoader.bundle

        return when (errorCode) {
            ErrorCode.ALREADY_EXISTS -> format(errorCode, bundle.getString("itemAlreadyExistsError"))
            ErrorCode.NOT_EXIST -> format(errorCode, bundle.getString("itemDoesNotExistError"))
            else -> {
                super.mapErrorCodeToMessage(errorCode)
            }
        }
    }
}