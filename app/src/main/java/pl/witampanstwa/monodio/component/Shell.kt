package pl.witampanstwa.monodio.component

import pl.witampanstwa.monodio.enum.Command


class Shell {

    external fun execute(
        command: String,
        user: Int = Command.User.PRIVILEGED.value
    ): CommandResult

    companion object {
        /**
         * Load the native call library.
         */
        init {
            System.loadLibrary("monodio")
        }

        /**
         * Checks for a boolean shell response.
         * If none found, exception is thrown.
         */
        fun parseToBoolean(response: String): Boolean {
            val cleanResponse = response.trim()

            if (cleanResponse == "1" || cleanResponse == "0") {
                return Integer.parseInt(cleanResponse) != 0
            } else {
                throw Exception("Invalid response (\"$cleanResponse\")")
            }
        }
    }
}
