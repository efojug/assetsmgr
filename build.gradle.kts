// Top-level build file where you can add configuration options common to all sub-projects/modules.
import java.util.*
val versionProps = Properties().apply {
    file("version.properties").inputStream().use { load(it) }
}
val versionCode: Int = versionProps.getProperty("VERSION_CODE", "1").toInt()

fun incrementVersionCode() {
    versionProps.setProperty("VERSION_CODE", "${versionCode + 1}")
    file("version.properties").outputStream().use { versionProps.store(it, null) }
}

plugins {
    id("com.android.application") version "8.1.1" apply false
    id("org.jetbrains.kotlin.android") version "1.9.10" apply false
}