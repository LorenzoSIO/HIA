package fr.lorenzocacciato.hialib.persistence

interface LoginListener {
    fun onLoginSuccess()
    fun onLoginFailed()
}