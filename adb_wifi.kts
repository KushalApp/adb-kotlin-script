import kotlin.system.measureTimeMillis
import java.util.Scanner

/**
 * Kotlin Script to automate wifi adb connection with the connected device:
 *
 * 1. retrieve the connected device IP address
 * 2. perform tcpip port forwarding
 * 3. connect to the ip address retrieved
 *
 */

val process = Runtime.getRuntime().exec("adb shell ip addr show wlan0")

val stream = process.inputStream
val errStream = process.errorStream

errStream?.let {
    val errorScanner = Scanner(it)
    errorScanner.useDelimiter("\n")

    while (errorScanner.hasNext()) {
        println(errorScanner.next())
    }
}

var myIp: String = ""
val scanner = Scanner(stream)
scanner.useDelimiter("\n")
while (scanner.hasNext()) {
    val str = scanner.next().trim()
    if (str.startsWith("inet ")) {
        var startIndex = str.indexOf("inet ")
        startIndex += "inet ".length
        val pos = str.indexOf('/')
        myIp = str.substring(startIndex, pos)
        if (myIp.length > 0) {
            break
        }
    }
}

if (myIp.isNotEmpty()) {
    Runtime.getRuntime().exec("adb tcpip 5555")
    Runtime.getRuntime().exec("adb connect $myIp")
}



