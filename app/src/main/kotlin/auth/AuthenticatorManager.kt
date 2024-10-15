package auth

/**
 * Синтаксис для валидации данных регистрации/авторизации
 * Является обёрткой над Authenticator @see Authenticator
 * Класс проверяет введённое имя, логин и пароль
 *
 * Имя является верным если не пустое
 * Логин является верным если не пустой и содержит '@'
 * Пароль является корректным если содержит более пяти символов
 */
class AuthenticatorManager(private val authenticator: Authenticator = GrpcAuthenticator()){
    /**
     * Валидация имени
     * @param name имя пользователя
     */
    private fun invalidateName(name: String): Result<String> {
        if (name.isEmpty()) {
            return Result.failure(Exception("Имя пользователя пустое"))
        }

        return Result.success(name)
    }

    /**
     * Валидация логина
     * @param login логин пользователя
     */
    private fun invalidateLogin(login: String): Result<String> {
        if(login.isEmpty()){
            return Result.failure(Exception("Логин пользователя пуст"))
        }

        if ('@' !in login) {
            return Result.failure(Exception("Логин не содержит '@'"))
        }

        return Result.success(login)
    }

    /**
     * Валидация пароля пользователя
     * @param password пароль пользователя
     */
    private fun invalidatePassword(password: String): Result<String> {
        if (password.length <= 5) {
            return Result.failure(Exception("Пароль должен содержать больше 5 символов"))
        }

        return Result.success(password)
    }

    /**
     * Функция для регистрации нового пользователя
     * Проверяет корректность входных параметров
     * @param name имя пользователя - непустая строка
     * @param login логин новой учетной записи - непустая строка
     * @param password пароль новой учетной записи - строка длиннее 5и символов
     * @return Result с сообщением об успехе или ошибке
     */
    suspend fun register(name: String, login: String, password: String): Result<String> {
        return invalidateName(name).fold(
            onSuccess = { _ ->
                invalidateLogin(login).fold(
                    onSuccess = { _ ->
                        invalidatePassword(password).fold(
                            onSuccess = { return authenticator.register(name, login, password) },
                            onFailure = { error -> Result.failure(error) }
                        )
                    },
                    onFailure = { error -> Result.failure(error) }
                )
            },
            onFailure = { error -> Result.failure(error) }
        )
    }

    /**
     * Функция авторизации пользователя
     * Проверяет корректность входных параметров
     * @param login логин пользователя
     * @param password пароль пользователя
     * @return Result с сообщением об успехе или ошибке
     */
    suspend fun login(login: String, password: String): Result<String> {
        return invalidateLogin(login).fold(
            onSuccess = { _ ->
                invalidatePassword(password).fold(
                    onSuccess = { return authenticator.login(login, password) },
                    onFailure = { error -> Result.failure(error) }
                )
            },
            onFailure = { error -> Result.failure(error) }
        )
    }
}