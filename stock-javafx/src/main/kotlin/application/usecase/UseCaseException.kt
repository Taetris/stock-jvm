package application.usecase

class UseCaseException(override var message: String, errorCode: ErrorCode): Exception(message) {

    constructor(errorCode: ErrorCode) : this("", errorCode)
}