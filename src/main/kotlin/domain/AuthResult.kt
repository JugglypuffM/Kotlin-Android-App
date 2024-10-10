package domain

/**
 * Результат запроса на аутентификацию
 * @param success true если запрос завершился успешно и false если нет
 * @param message сообщение об успехе или ошибке
 */
data class AuthResult(
    val success: Boolean,
    val message: String
)