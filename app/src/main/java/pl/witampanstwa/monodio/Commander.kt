package pl.witampanstwa.monodio

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast


class Commander : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val audioSettings = AudioSettings()

        try {
            val shell = Shell()

            audioSettings.mono = Shell.parseToBoolean(shell.execute(
                    "settings get system master_mono"))

            audioSettings.mono = !audioSettings.mono!!
            shell.execute(
                    "settings put system master_mono $audioSettings.mono")

            Toast.makeText(applicationContext,
                    "Mono: $audioSettings.mono", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(applicationContext, e.toString(), Toast.LENGTH_LONG).show()
        }
    }
}
