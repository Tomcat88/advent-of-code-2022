fun main() {

    fun part1(input: List<String>) =
        input.sumOf {
            val h1 = it.slice(0 until (it.length / 2)).toSet()
            val h2 = it.slice((it.length / 2) until (it.length)).toSet()
            h1.intersect(h2).firstOrNull()?.toPriority() ?: 0
        }

    fun part2(input: List<String>) =
        input.chunked(3).sumOf {
            it.map { it.toSet() }.reduce { acc, e -> acc.intersect(e) }.firstOrNull()?.toPriority() ?: 0
        }

    val input = readInput("Day03")
    println(part1(input))
    println(part2(input))
}

fun Char.toPriority() = if (isUpperCase()) {
    code - 38
} else {
    code - 96
}
