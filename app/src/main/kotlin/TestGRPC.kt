import auth.Authenticator
import auth.GrpcAuthenticator

suspend fun main() {
    val authenticator: Authenticator = GrpcAuthenticator()
    println(authenticator.register("stas", "jpf", "123456"))
    println(authenticator.register("stas", "jpf", "123465"))
    println(authenticator.login("jpf", "123456"))
    println(authenticator.register("stas", "jpf", "1234"))
    println(authenticator.login("jpf", "1234"))
}