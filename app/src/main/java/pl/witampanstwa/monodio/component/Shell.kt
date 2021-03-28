package pl.witampanstwa.monodio.component

import android.content.res.Resources
import android.util.Log
import pl.witampanstwa.monodio.R
import pl.witampanstwa.monodio.exception.PrivilegeException
import java.io.*


class Shell {
//    private val shell: Process = Runtime.getRuntime().exec("su")
//    private val outputStream = DataOutputStream(this.shell.outputStream)

    init {
        Log.d("Commander", "initialised Shell")
    }

    /**
     * Writes a shell command onto the provided stream
     * and if applicable returns shell response.
     */
    fun execute(instruction: String, provideOutput: Boolean = true): String {
        val shell: Process = Runtime.getRuntime().exec("su")
        val outputStream = DataOutputStream(shell.outputStream)

        Log.d("Commander", "Began execution of $instruction")
        outputStream.writeBytes("$instruction\n")
        outputStream.flush()

        if (provideOutput) {
            val bufferedReader = BufferedReader(
                InputStreamReader(shell.inputStream)
            )

            val output = bufferedReader.readLine()
            Log.d("Commander", "Read: $output")

            bufferedReader.close()

            if (output == null) {
                throw PrivilegeException(
                    "Konfiguracja telefonu odmawia uprawnień aplikacji!"
                )
            }
            return output.toString()
        } else {
            return "No output requested"
        }
    }

    companion object {
        /**
         * Checks for a boolean shell response.
         * If none found, exception is thrown.
         */
        fun parseToBoolean(response: String): Boolean {
            if (response == "1" || response == "0") {
                return Integer.parseInt(response) != 0
            } else {
                throw Exception("Empty/invalid response (\"$response\")")
            }
        }
    }
}
