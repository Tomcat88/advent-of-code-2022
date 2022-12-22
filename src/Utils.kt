import java.io.File
import java.math.BigInteger
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse.BodySubscribers
import java.nio.charset.Charset
import java.nio.file.Files
import java.nio.file.Path
import java.security.MessageDigest

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = File("src", "$name.txt").readLines()

fun readInput(name: String, delimiter: String) = File("src", "$name.txt").readText().split(delimiter)

fun readInputAndSplit(name: String, delimiter: String = " ") = File("src", "$name.txt").readLines().map { it.split(delimiter) }

fun readInputAndSplitInPairs(name: String, delimiter: String = " ") = File("src", "$name.txt").readLines().map { it.split(delimiter).let { it.first() to it[1] } }

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray())).toString(16).padStart(32, '0')

private var _logIndex = 0
private var startTime = System.currentTimeMillis()
fun <T> T.log(prefix: String = ""): T = also { println("[${(System.currentTimeMillis() - startTime).toString().padStart(4)} ms] %03d %03d:${prefix.padStart(8)} %s".format(_logIndex / 1000, _logIndex++ % 1000, this)) }

fun downloadAndReadInput(day: String, delimiter: String = "\n"): List<String> {
    if (!Files.exists(Path.of("./src/${day}.txt"))) {
        downloadInput(day.removePrefix("Day"))
    }
    return readInput(day, delimiter)
}
fun downloadInput(day: String) {
    val url = "https://adventofcode.com/2022/day/${day.toInt()}/input"
    val session = System.getenv("SESSION") ?: error("SESSION is mandatory for downloading input")
    val req = HttpRequest.newBuilder(URI.create(url)).GET().header("Cookie", "session=$session").build()
    HttpClient.newHttpClient().send(req) { resp ->
        return@send if (resp.statusCode() == 200) {
            BodySubscribers.ofFile(Path.of("./src/Day${day.padStart(2, '0')}.txt"))
        } else {
            error("Could not download input ${resp.statusCode()} $resp")
        }
    }.body()
}

typealias IntPair = Pair<Int, Int>
