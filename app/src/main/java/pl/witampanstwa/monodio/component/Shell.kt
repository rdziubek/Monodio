package pl.witampanstwa.monodio.component

import android.util.Log
import pl.witampanstwa.monodio.enum.Command
import pl.witampanstwa.monodio.exception.PrivilegeException


class Shell {

    fun execute(command: String, user: Int = Command.User.PRIVILEGED.value): CommandResult {
        if (!privilegedBinaryPresent()) {
            val exception = PrivilegeException(
                PrivilegeException.Message.INCORRECT_CONFIG.value
            )

            Log.e(
                Logger.SYMBOL,
                null, exception
            )
            throw exception
        }

        val result: CommandResult = this.executeNative(command, user)
        if (result.returnCode == Command.ReturnCode.DENIED.value ||
            result.returnCode == Command.ReturnCode.BAD_USAGE.value
        ) {
            val exception = PrivilegeException(
                PrivilegeException.Message.INSUFFICIENT_PERMISSIONS.value
            )

            Log.e(
                Logger.SYMBOL,
                result.toString(), exception
            )
            throw exception
        }

        return result
    }

    private fun privilegedBinaryPresent(): Boolean {
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
        fun parseToBoolean(responseRaw: String): Boolean {
            val response = responseRaw.trim()

            if (response == Response.TRUE || response == Response.FALSE) {
                return Integer.parseInt(response) != 0
            } else {
                throw Exception("Invalid response (\"$response\")")
            }
        }

        object Response {
            const val TRUE: String = "1"
            const val FALSE: String = "0"
        }

        object Logger {
            const val SYMBOL = "ShellChannel"
        }
    }
}
