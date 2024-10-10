import auth.Authenticator

fun main() {
    val authenticator: Authenticator = Authenticator("localhost", 50051)
    println(authenticator.register("stas", "jpf", "123456"))
    println(authenticator.login("jpf", "123456"))
    println(authenticator.register("stas", "jpf", "1234"))
    println(authenticator.login("jpf", "1234"))
}