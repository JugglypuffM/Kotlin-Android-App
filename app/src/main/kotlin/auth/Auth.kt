package auth

/**
 * Реализация дефолтной аутентификации
 */
object Auth: Authenticator by AuthenticationWithValidation(GrpcAuthenticator())