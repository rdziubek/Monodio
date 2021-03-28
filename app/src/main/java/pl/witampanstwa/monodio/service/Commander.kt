package pl.witampanstwa.monodio.service

import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import android.widget.Toast
import pl.witampanstwa.monodio.component.AudioSettings
import pl.witampanstwa.monodio.component.Shell


class Commander : TileService() {
    private val shell = Shell()

    override fun onClick() {
        super.onClick()

        val audioSettings = AudioSettings(shell)

        audioSettings.flipMono()

        val tile = qsTile
        tile.state = if (audioSettings.mono) Tile.STATE_ACTIVE else Tile.STATE_INACTIVE
        tile.updateTile()

        val toast =
            Toast.makeText(applicationContext, "mono: ${audioSettings.mono}", Toast.LENGTH_SHORT)
        toast.show()
    }
}
