package domain

/**
 * Результат запроса на аутентификацию
 * @param message сообщение об успехе или ошибке
 */
sealed class AuthResult {
    /**
     * Случай успешной аутентификации
     * @param message сообщение об успехе
     */
    data class Success(val message: String) : AuthResult()
    /**
     * Случай неудачной аутентификации
     * @param message сообщение об ошибке
     */
    data class Failure(val message: String) : AuthResult()
}