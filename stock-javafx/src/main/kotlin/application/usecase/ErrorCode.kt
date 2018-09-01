package application.usecase

enum class ErrorCode(val code: Int) {
    ALREADY_EXISTS(400),
    NOT_EXIST(401),
    INVALID_INPUT(402),
    OPERATION_FAILED(501),
}