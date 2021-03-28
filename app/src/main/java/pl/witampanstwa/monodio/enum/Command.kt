package pl.witampanstwa.monodio.enum


enum class Command {
    COMMAND;

    enum class ReturnCode(val value: Int) {
        GOOD(0),
        INSUFFICIENT_PRIVILEGES(255),
        NO_SUCH_COMMAND(127),
        DENIED(13),
    }

    enum class User(val value: Int) {
        DEFAULT(0),
        PRIVILEGED(1),
    }
}
