package pl.witampanstwa.monodio

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import pl.witampanstwa.monodio.component.CommandResult

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val command: String = "settings"
        val userPrivileged = 1
        val userNormal = 0

        val result = execute(
            command,
            userNormal
        )

        Log.d(
            "CommanderJava",
            "onCreate: code: ${result.returnCode} out: ${result.stdOut} err: ${result.stdErr}"
        )
    }

    external fun execute(command: String, user: Int): CommandResult

    companion object {
        init {
            System.loadLibrary("monodio")
        }
    }
}
