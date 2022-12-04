fun main() {

    fun getRangeSets(input: List<Pair<String, String>>) = input.map { (first, second) ->
        first.toRangeSet() to second.toRangeSet()
    }

    fun part1(input: List<Pair<String, String>>) = getRangeSets(input).count { (f, s) ->
        (f union s).size in listOf(f.size, s.size)
    }

    fun part2(input: List<Pair<String, String>>): Int = getRangeSets(input).count { (f, s) ->
        (f union s).size < f.size + s.size
    }

    val input = readInputAndSplitInPairs("Day04", ",")
    println(part1(input))
    println(part2(input))
}

fun String.toRangeSet() = split("-").let {
    it[0].toInt()..it[1].toInt()
}.toSet()

