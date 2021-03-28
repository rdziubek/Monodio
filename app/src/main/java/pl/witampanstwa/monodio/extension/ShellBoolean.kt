package pl.witampanstwa.monodio.extension

class ShellBoolean(boolean: Boolean) {
    val value: Int = if (boolean) 1 else 0
}
