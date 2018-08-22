package application.usecase

class UseCaseException(override var message: String): Exception(message) {

    constructor(message: String, throwable: Throwable) : this(message) {
        Exception(message, throwable)
    }
}