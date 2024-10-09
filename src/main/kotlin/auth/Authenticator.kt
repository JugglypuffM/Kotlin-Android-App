package auth

import io.grpc.ManagedChannelBuilder
import org.example.grpc.AuthProto.LoginRequest
import org.example.grpc.AuthProto.RegisterRequest
import org.example.grpc.AuthServiceGrpc

class Authenticator(private val address: String, private val port: Int) {
    fun register(name: String, login: String, password: String): String {
        val channel = ManagedChannelBuilder.forAddress(address, port)
            .usePlaintext()
            .build()

        val stub = AuthServiceGrpc.newBlockingStub(channel)

        val registerRequest = RegisterRequest.newBuilder()
            .setName(name)
            .setLogin(login)
            .setPassword(password)
            .build()

        return stub.register(registerRequest).message
    }

    fun login(login: String, password: String): String {
        val channel = ManagedChannelBuilder.forAddress(address, port)
            .usePlaintext()
            .build()

        val stub = AuthServiceGrpc.newBlockingStub(channel)

        val loginRequest = LoginRequest.newBuilder()
            .setLogin(login)
            .setPassword(password)
            .build()

        return stub.login(loginRequest).message
    }
}