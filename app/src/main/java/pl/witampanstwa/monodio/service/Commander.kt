package pl.witampanstwa.monodio.service

import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import android.util.Log
import android.widget.Toast
import pl.witampanstwa.monodio.component.AudioSettings
import pl.witampanstwa.monodio.component.Shell
import pl.witampanstwa.monodio.component.CommandResult
import pl.witampanstwa.monodio.enum.Command


class Commander : TileService() {
    private val shell = Shell()

    override fun onClick() {
        super.onClick()
        Log.d("Commander", "seen click")

        val commandResult = execute(
            "settings",
            Command.User.DEFAULT.value
        )

        Log.d(
            "CommanderJava",
            "onCreate: code: ${commandResult.returnCode} out: ${commandResult.stdOut} err: ${commandResult.stdErr}"
        )

        val tile = qsTile
        val audioSettings = AudioSettings(shell)

        try {
            audioSettings.flipMono()

            tile.state = if (audioSettings.mono) Tile.STATE_ACTIVE else Tile.STATE_INACTIVE
            tile.updateTile()
        } catch (e: Exception) {
            Log.e("Commander", e.toString())
        }

        val toast =
            Toast.makeText(applicationContext, "mono: ${audioSettings.mono}", Toast.LENGTH_SHORT)
        toast.show()
    }

    private external fun execute(
        command: String,
        user: Int = Command.User.PRIVILEGED.value
    ): CommandResult

    companion object {
        init {
            System.loadLibrary("monodio")
        }
    }
}
