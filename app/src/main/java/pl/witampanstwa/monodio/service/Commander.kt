package pl.witampanstwa.monodio.service

import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import android.util.Log
import android.widget.Toast
import pl.witampanstwa.monodio.component.AudioSettings
import pl.witampanstwa.monodio.component.Shell


class Commander : TileService() {
    private val shell = Shell()

    override fun onClick() {
        super.onClick()

        Log.d("Commander", "seen click")

        val tile = qsTile

        val audioSettings = AudioSettings(shell)

        try {
            Log.d("Commander", "Returned from Shell")

            audioSettings.flipMono()
            tile.state = if (audioSettings.mono) Tile.STATE_ACTIVE else Tile.STATE_INACTIVE
            tile.updateTile()
        } catch (e: Exception) {
            Log.d("Commander", e.toString())
        }

        val text = "Did it!"
        val duration = Toast.LENGTH_SHORT
        val toast = Toast.makeText(applicationContext, text, duration)
        toast.show()
    }
}