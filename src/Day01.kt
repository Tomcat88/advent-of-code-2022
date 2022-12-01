fun main() {

    fun getWeights(input: List<String>) = input.fold(listOf(0)) { acc, s ->
        if (s.isBlank()) {
            acc + 0
        } else {
            acc.dropLast(1) + (acc.last() + s.toInt())
        }
    }

    fun part1(input: List<String>): Int {
        return getWeights(input).max()
    }

    fun part2(input: List<String>): Int {
        return getWeights(input).sortedDescending().take(3).sum()
    }

    val input = readInput("Day01")
    println(part1(input))
    println(part2(input))
}
