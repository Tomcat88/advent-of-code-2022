fun main() {

    fun part1(input: List<String>) =
        input.sumOf { rucksack ->
            rucksack.chunked(rucksack.length / 2)
                    .map { it.toSet() }
                    .reduce { acc, e -> acc intersect e }
                    .singleOrNull().priority
        }

    fun part2(input: List<String>) =
        input.chunked(3).sumOf { rucksack ->
            rucksack.map { it.toSet() }
              .reduce { acc, e -> acc intersect e }
              .singleOrNull().priority
        }

    val input = readInput("Day03")
    println(part1(input))
    println(part2(input))

}
val Char?.priority
    get() = when(this) {
       null -> 0
       in 'a'..'z' -> code - 'a'.code + 1
       in 'A'..'Z' -> code - 'A'.code + 27
       else -> error("invalid char: $this")
    }
