import auth.Authenticator

fun main() {
    val authenticator: Authenticator = Authenticator("localhost", 50051)
    print(authenticator.register("stas", "jpf", "123456"))
    print(authenticator.login("jpf", "123456"))
}