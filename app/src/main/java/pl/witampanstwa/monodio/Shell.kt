package pl.witampanstwa.monodio

import java.io.BufferedReader
import java.io.InputStreamReader


class Shell {
    private val shell: Runtime = Runtime.getRuntime()

    /**
     * Writes a shell command onto the provided stream
     * and if applicable returns shell response.
     */
    fun execute(instruction: String): String {
        val process = shell.exec("$instruction\n")

        val bufferedReader = BufferedReader(
                InputStreamReader(process.inputStream))

        val result = StringBuilder()
        var line: String? = ""
        while (bufferedReader.readLine().also { line = it } != null) {
            result.append(line)
        }

        return result.toString()
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
