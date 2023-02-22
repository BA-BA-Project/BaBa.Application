package kids.baba.mobile.core.error

import kids.baba.mobile.domain.model.TokenResponse

class UserNotFoundException(val tokenResponse: TokenResponse, message: String): Exception(message)
