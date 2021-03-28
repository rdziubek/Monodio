package pl.witampanstwa.monodio.component

import android.util.Log
import pl.witampanstwa.monodio.enum.Command
import pl.witampanstwa.monodio.extension.ShellBoolean

/**
 * Acts as a buffer between the android framework and the application's logic.
 *
 * NOTE: Settings object parameter retrieval – not to do it all through a relatively slow
 *  communication channel – is done dynamically as the once retrieved android framework's value
 *  will not change throughout application lifetime; parameter setting is on the other hand done
 *  directly, as there is no room for further optimisation.
 */
class AudioSettings(private val communicationChannel: Shell) {
    var mono: Boolean =
        Shell.parseToBoolean(
            communicationChannel.execute(
                "settings get system master_mono"
            ).stdOut
        )
        private set(value) {
            communicationChannel.execute(
                "settings put system master_mono ${ShellBoolean(value).value}"
            )
            field = value
        }

    fun flipMono() {
        Log.d("Commander", "flipping setting")

        Log.d("Commander", "Currently at: ${this.mono}, going into: ${!this.mono}")
        this.mono = !this.mono
    }
}
