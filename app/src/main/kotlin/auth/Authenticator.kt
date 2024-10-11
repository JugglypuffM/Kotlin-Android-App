package auth

import io.grpc.ManagedChannelBuilder
import domain.AuthResult
import io.grpc.StatusRuntimeException
import org.example.grpc.AuthProto.LoginRequest
import org.example.grpc.AuthProto.RegisterRequest
import org.example.grpc.AuthServiceGrpc

/**
 * Класс для отправки запросов аутентификации на сервер по gRPC
 * @param address адрес сервера
 * @param port порт сервера
 */
class Authenticator(private val address: String, private val port: Int) {
    /**
     * Функция для регистрации нового пользователя
     * @param name имя пользователя - непустая строка
     * @param login логин новой учетной записи - непустая строка
     * @param password пароль новой учетной записи - строка длиннее 5и символов
     * @return результат запроса AuthResult
     */
    fun register(name: String, login: String, password: String): AuthResult {
        val channel = ManagedChannelBuilder.forAddress(address, port)
            .usePlaintext()
            .build()

        val stub = AuthServiceGrpc.newBlockingStub(channel)

        val registerRequest = RegisterRequest.newBuilder()
            .setName(name)
            .setLogin(login)
            .setPassword(password)
            .build()

        try{
            val response = stub.register(registerRequest)

            return AuthResult(response.success, response.message)
        }
        catch (_: StatusRuntimeException){
            return AuthResult(false, "Failed to connect to the server")
        }
    }


    /**
     * Функция авторизации пользователя
     * @param login логин пользователя
     * @param password пароль пользователя
     * @return результат запроса AuthResult
     */
    fun login(login: String, password: String): AuthResult {
        val channel = ManagedChannelBuilder.forAddress(address, port)
            .usePlaintext()
            .build()

        val stub = AuthServiceGrpc.newBlockingStub(channel)

        val loginRequest = LoginRequest.newBuilder()
            .setLogin(login)
            .setPassword(password)
            .build()

        try{
            val response = stub.login(loginRequest)

            return AuthResult(response.success, response.message)
        }
        catch (_: StatusRuntimeException){
            return AuthResult(false, "Failed to connect to the server")
        }
    }
}