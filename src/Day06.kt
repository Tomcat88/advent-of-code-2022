fun main() {

    fun findMarker(input: String, n: Int) =
        input.windowed(n).indexOfFirst { it.toCharArray().distinct().size == it.length } + n

    fun part1(input: String) {
        findMarker(input, 4).log("part1")
    }

    fun part2(input: String) {
        findMarker(input, 14).log("part2")
    }

    readInput("Day06").forEach { part1(it); part2(it) }
}


