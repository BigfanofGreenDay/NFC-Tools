package cc.metapro.nfc.util

import android.content.Context
import android.content.res.AssetManager
import java.io.BufferedReader
import java.io.Closeable
import java.io.IOException
import java.io.InputStreamReader

fun readAll(br: BufferedReader, timeout: Long = -1): String {
    var line = br.readLine()
    val sb = StringBuilder()
    // no timeout
    if (timeout == (-1).toLong()) {
        while (line != null) {
            sb.append(line).append('\n')
            line = br.readLine()
        }
        return sb.toString()
    } else {
        val maxTimeMillis = System.currentTimeMillis() + timeout
        while (System.currentTimeMillis() < maxTimeMillis && line != null) {
            sb.append(line).append('\n')
            line = br.readLine()
        }
        return sb.toString()
    }
}

@Throws(IOException::class)
fun AssetManager.readAll(fileName: String): String {
    var br: BufferedReader? = null
    try {
        br = BufferedReader(InputStreamReader(this.open(fileName)))
        return cc.metapro.nfc.util.readAll(br)
    } finally {
        close(br)
    }
}

@Throws(IOException::class)
fun Context.readAll(fileName: String): String {
    var br: BufferedReader? = null
    try {
        br = BufferedReader(InputStreamReader(this.openFileInput(fileName)))
        return cc.metapro.nfc.util.readAll(br)
    } finally {
        close(br)
    }
}

@Throws(IOException::class)
fun Context.writeAll(fileName: String) {

}

fun close(vararg c: Closeable?) {
    c.filterNotNull().forEach {
        try {
            it.close()
        } catch (e: Exception) {
        }
    }
}