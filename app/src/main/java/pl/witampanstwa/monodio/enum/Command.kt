package pl.witampanstwa.monodio.enum


enum class Command(val value: String) {
    COMMAND_GET("settings get system master_mono"),
    COMMAND_SET("settings put system master_mono");

    enum class ReturnCode(val value: Int) {
        GOOD(0),
        BAD_USAGE(255),
        NO_SUCH_COMMAND(127),
        DENIED(13),
    }

    enum class User(val value: Int) {
        DEFAULT(0),
        PRIVILEGED(1),
    }
}
