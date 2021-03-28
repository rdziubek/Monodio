package pl.witampanstwa.monodio.component

import pl.witampanstwa.monodio.enum.Command
import pl.witampanstwa.monodio.exception.PrivilegeException


class Shell {

    fun execute(command: String, user: Int = Command.User.PRIVILEGED.value): CommandResult {
        if (!isPrivilegedBinaryPresent()) {
            throw PrivilegeException("Konfiguracja urządzenia jest nieodpowiednia")
        }

        val result: CommandResult = this.executeNative(command, user)

        if (result.returnCode == Command.ReturnCode.DENIED.value ||
            result.returnCode == Command.ReturnCode.INSUFFICIENT_PRIVILEGES.value
        ) {
            throw PrivilegeException("Aplikacji nie przydzielono wystarczających uprawnień")
        }

        return result
    }

    private fun isPrivilegedBinaryPresent(): Boolean {
        val test: CommandResult = this.executeNative("su", Command.User.DEFAULT.value)

        if (test.returnCode == Command.ReturnCode.NO_SUCH_COMMAND.value) {
            return false
        }
        return true
    }

    private external fun executeNative(command: String, user: Int): CommandResult

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
