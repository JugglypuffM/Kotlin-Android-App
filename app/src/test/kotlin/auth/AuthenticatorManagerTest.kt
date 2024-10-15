package auth

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class AuthenticatorManagerTest {
    private lateinit var authenticatorStub: Authenticator
    private lateinit var authenticatorManager: AuthenticatorManager

    val successServerResult = Result.success("хороший ответ от сервера")

    @Before
    fun setUp() {
        authenticatorStub = mockk<Authenticator>()
        authenticatorManager = AuthenticatorManager(authenticatorStub)
    }

    @Test
    fun `valid parameters for registration`() {
        runBlocking {
            coEvery { authenticatorStub.register(any(), any(), any()) } returns successServerResult

            val name = "user"
            val login = "user@mail.com"
            val password = "123456"

            val expected = successServerResult
            val actual = authenticatorManager.register(name, login, password)

            coVerify { authenticatorStub.register(name, login, password) }
            assertEquals(expected, actual)
        }
    }



    @Test
    fun `valid parameters for login`() {
        runBlocking {
            coEvery { authenticatorStub.login(any(), any()) } returns successServerResult

            val login = "user@mail.com"
            val password = "123456"

            val expected = successServerResult
            val actual = authenticatorManager.login(login, password)

            coVerify { authenticatorStub.login(login, password) }
            assertEquals(expected, actual)
        }
    }

    @Test
    fun `empty username`() {
        runBlocking {
            val name = ""
            val login = "user@mail.com"
            val password = "123456"

            val expectedErrorMessage = "Имя пользователя пустое"
            val actual = authenticatorManager.register(name, login, password)

            coVerify (exactly = 0){ authenticatorStub.register(name, login, password) }
            assert(actual.isFailure)
            assertEquals(expectedErrorMessage, actual.exceptionOrNull()?.message)
        }
    }

    @Test
    fun `empty login`() {
        runBlocking {
            val name = "user"
            val login = ""
            val password = "123456"

            val expectedErrorMessage = "Логин пользователя пуст"
            val actual = authenticatorManager.register(name, login, password)

            coVerify (exactly = 0){ authenticatorStub.register(name, login, password) }
            assert(actual.isFailure)
            assertEquals(expectedErrorMessage, actual.exceptionOrNull()?.message)
        }
    }

    @Test
    fun `incorrect login`() {
        runBlocking {
            val name = "user"
            val login = "usermail.com"
            val password = "123456"

            val expectedErrorMessage = "Логин не содержит '@'"
            val actual = authenticatorManager.register(name, login, password)

            coVerify (exactly = 0){ authenticatorStub.register(name, login, password) }
            assert(actual.isFailure)
            assertEquals(expectedErrorMessage, actual.exceptionOrNull()?.message)
        }
    }

    @Test
    fun `short password`() {
        runBlocking {
            val name = "user"
            val login = "user@mail.com"
            val password = "12345"

            val expectedErrorMessage = "Пароль должен содержать больше 5 символов"
            val actual = authenticatorManager.register(name, login, password)

            coVerify(exactly = 0) { authenticatorStub.register(name, login, password) }
            assert(actual.isFailure)
            assertEquals(expectedErrorMessage, actual.exceptionOrNull()?.message)
        }
    }
}