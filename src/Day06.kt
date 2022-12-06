fun main() {

    fun findMarker(input: String, n: Int) {
        input.windowed(n).mapIndexed { index, c -> Pair(c.toCharArray().distinct().size == c.length, index + c.length) }.first { it.first }.second
    }

    fun part1(input: String) {
        findMarker(input, 4).log("part1")
    }

    fun part2(input: String) {
        findMarker(input, 14).log("part2")
    }

    readInput("Day06").forEach { part1(it); part2(it) }
}


