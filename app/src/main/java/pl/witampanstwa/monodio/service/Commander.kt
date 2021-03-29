package pl.witampanstwa.monodio.service

import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import android.widget.Toast
import pl.witampanstwa.monodio.component.AudioSettings
import pl.witampanstwa.monodio.component.Shell
import pl.witampanstwa.monodio.exception.PrivilegeException

class Commander : TileService() {
    private val shell = Shell()

    override fun onClick() {
        super.onClick()

        lateinit var audioSettings: AudioSettings

        try {
            audioSettings = AudioSettings(shell)
            audioSettings.flipMono()
            updateTile(qsTile, audioSettings.mono)
        } catch (e: PrivilegeException) {
            Toast.makeText(
                baseContext, "${e.javaClass.kotlin}\n\n${e.message}",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    companion object {
        @JvmStatic
        fun updateTile(tile: Tile, litUp: Boolean) {
            tile.state = if (litUp) Tile.STATE_ACTIVE else Tile.STATE_INACTIVE
            tile.updateTile()
        }
    }
}
