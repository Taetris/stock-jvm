package application.usecase

import application.ResourceLoader

open class ErrorCodeMapper {

    protected val bundle = ResourceLoader.bundle

    open fun mapErrorCodeToMessage(errorCode: ErrorCode): String {
        val message = when (errorCode) {
            ErrorCode.ALREADY_EXISTS -> bundle.getString("defaultError")
            ErrorCode.NOT_EXIST -> bundle.getString("defaultError")
            ErrorCode.INVALID_INPUT -> bundle.getString("invalidInputError")
            ErrorCode.OPERATION_FAILED -> bundle.getString("operationFailedError")
        }

        return format(errorCode, message)
    }

    protected fun format(errorCode: ErrorCode, message: String): String {
        return "Error: ${errorCode.code}\n$message"
    }
}