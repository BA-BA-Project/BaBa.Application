package kids.baba.mobile.core.error

class UserNotFoundException(val signToken: String, message: String): Exception(message)
