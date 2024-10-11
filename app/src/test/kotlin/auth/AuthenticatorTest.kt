package auth

import io.grpc.ManagedChannel
import io.grpc.StatusRuntimeException
import io.mockk.*
import org.example.grpc.AuthProto.*
import org.example.grpc.AuthServiceGrpc
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class AuthenticatorTest {

    private lateinit var authenticator: Authenticator
    private lateinit var mockStub: AuthServiceGrpc.AuthServiceBlockingStub
    private lateinit var mockChannel: ManagedChannel

    @BeforeEach
    fun setUp() {
        mockChannel = mockk()
        mockStub = mockk()

        authenticator = spyk(Authenticator("localhost", 50051)) {
            every { AuthServiceGrpc.newBlockingStub(any()) } returns mockStub
        }
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `register should return success result on successful registration`() {
        val expectedResponse = AuthResponse.newBuilder()
            .setSuccess(true)
            .setMessage("User registered successfully")
            .build()

        every { mockStub.register(any()) } returns expectedResponse

        val result = authenticator.register("John", "john123", "password123")
        assertEquals(true, result.success)
        assertEquals("User registered successfully", result.message)
    }

    @Test
    fun `register should return failure result when exception occurs`() {
        every { mockStub.register(any()) } throws StatusRuntimeException(mockk())

        val result = authenticator.register("John", "john123", "password123")
        assertEquals(false, result.success)
        assertEquals("Failed to connect to the server", result.message)
    }

    @Test
    fun `login should return success result on successful login`() {
        val expectedResponse = AuthResponse.newBuilder()
            .setSuccess(true)
            .setMessage("Login successful")
            .build()

        every { mockStub.login(any()) } returns expectedResponse

        val result = authenticator.login("john123", "password123")
        assertEquals(true, result.success)
        assertEquals("Login successful", result.message)
    }

    @Test
    fun `login should return failure result when exception occurs`() {
        every { mockStub.login(any()) } throws StatusRuntimeException(mockk())

        val result = authenticator.login("john123", "password123")
        assertEquals(false, result.success)
        assertEquals("Failed to connect to the server", result.message)
    }
}
