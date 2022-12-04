fun main() {

    fun part1(input: List<Pair<String, String>>) = input.map { (first, second) ->
        first.toRangeSet() to second.toRangeSet()
    }.map { (first, second) ->
        (first union second).size in listOf(first.size, second.size)
    }.count { it }

    fun part2(input: List<Pair<String, String>>): Int = input.map { (first, second) ->
        first.toRangeSet() to second.toRangeSet()
    }.map { (first, second) ->
        (first union second).size < first.size + second.size
    }.count { it }

    val input = readInputAndSplitInPairs("Day04", ",")
    println(part1(input))
    println(part2(input))
}

fun String.toRangeSet() = split("-").let {
    it[0].toInt()..it[1].toInt()
}.toSet()

