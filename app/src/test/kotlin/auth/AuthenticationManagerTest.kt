package auth

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class AuthenticationManagerTest {
    private lateinit var authenticatorStub: Authenticator
    private lateinit var authenticationManager: AuthenticationManager

    val successServerResult = Result.success("хороший ответ от сервера")

    @Before
    fun setUp() {
        authenticatorStub = mockk<Authenticator>()
        authenticationManager = AuthenticationManager(authenticatorStub)
    }

    @Test
    fun `valid parameters for registration`() {
        runBlocking {
            coEvery { authenticatorStub.register(any(), any(), any()) } returns successServerResult

            val name = "Гриша"
            val login = "MegaGrish1337"
            val password = "1234567"

            val expected = successServerResult
            val actual = authenticationManager.register(name, login, password)

            coVerify { authenticatorStub.register(name, login, password) }
            assert(actual.isSuccess)
            assertEquals(expected, actual)
        }
    }



    @Test
    fun `valid parameters for login`() {
        runBlocking {
            coEvery { authenticatorStub.login(any(), any()) } returns successServerResult

            val login = "MegaGrish1337"
            val password = "1234567"

            val expected = successServerResult
            val actual = authenticationManager.login(login, password)

            coVerify { authenticatorStub.login(login, password) }
            assert(actual.isSuccess)
            assertEquals(expected, actual)
        }
    }

    @Test
    fun `empty username`() {
        runBlocking {
            val name = ""
            val login = "MegaGrish1337"
            val password = "1234567"

            val actual = authenticationManager.register(name, login, password)

            coVerify (exactly = 0){ authenticatorStub.register(name, login, password) }
            assert(actual.isFailure)
            assert(actual.exceptionOrNull() is Authenticator.IncorrectNameException)
        }
    }

    @Test
    fun `empty login`() {
        runBlocking {
            val name = "Гриша"
            val login = ""
            val password = "1234567"

            val actual = authenticationManager.register(name, login, password)

            coVerify (exactly = 0){ authenticatorStub.register(name, login, password) }
            assert(actual.isFailure)
            assert(actual.exceptionOrNull() is Authenticator.IncorrectLoginException)
        }
    }

    @Test
    fun `short password`() {
        runBlocking {
            val name = "Гриша"
            val login = "MegaGrish1337"
            val password = "123456"

            val actual = authenticationManager.register(name, login, password)

            coVerify(exactly = 0) { authenticatorStub.register(name, login, password) }
            assert(actual.isFailure)
            assert(actual.exceptionOrNull() is Authenticator.IncorrectPasswordException)
        }
    }
}