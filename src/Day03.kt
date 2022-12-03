fun main() {

    fun part1(input: List<String>) =
        input.sumOf { racksack ->
            racksack.chunked(racksack.length / 2)
                    .map { it.toSet() }
                    .reduce { acc, e -> acc.intersect(e) }
                    .firstOrNull()?.toPriority() ?: 0
        }

    fun part2(input: List<String>) =
        input.chunked(3).sumOf {
            it.map { it.toSet() }
              .reduce { acc, e -> acc.intersect(e) }
              .firstOrNull()?.toPriority() ?: 0
        }

    val input = readInput("Day03")
    println(part1(input))
    println(part2(input))
}

fun Char.toPriority() = if (isUpperCase()) {
    code - 'A'.code + 27
} else {
    code - 'a'.code + 1
}
