package pl.witampanstwa.monodio.exception

class PrivilegeException(message: String) : Exception(message) {

    enum class Message(val value: String) {
        INCORRECT_CONFIG("Konfiguracja urządzenia jest nieodpowiednia"),
        INSUFFICIENT_PERMISSIONS("Aplikacji nie przydzielono wystarczających uprawnień");
    }
}
