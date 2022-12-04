fun main() {

    fun part1(input: List<Pair<String, String>>) =
       input.map { it.first.toRange().toSet() to it.second.toRange().toSet()}
            .map {
                (it.first union it.second).size in listOf(it.second.size, it.first.size)
            }.count { it }


    fun part2(input: List<Pair<String, String>>): Int =
        input.map { it.first.toRange().toSet() to it.second.toRange().toSet() }
             .map {
                 (it.first union it.second).size < it.second.size + it.first.size
             }.count { it }

    val input = readInputAndSplitInPairs("Day04", ",")
    println(part1(input))
    println(part2(input))
}

fun String.toRange() = split("-").let {
    it[0].toInt()..it[1].toInt()
}

