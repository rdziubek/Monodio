package pl.witampanstwa.monodio.enum


enum class Command {
    COMMAND;

    enum class ReturnCode(val returnCode: Int) {
        GOOD(0),
        INCORRECT_USAGE(255),
        NO_SUCH_COMMAND(127),
        DENIED(13),
    }

    enum class User(val userType: Int) {
        DEFAULT(0),
        PRIVILEGED(1),
    }
}
