package auth

object Auth: Authenticator by AuthenticationWithValidation(GrpcAuthenticator())