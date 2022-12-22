import kotlin.math.absoluteValue

object Day20 {

    fun mix(input: List<IndexedValue<Long>>): MutableList<IndexedValue<Long>> {
        val sorted = input.toMutableList()
        input.sortedBy { it.index }.forEach { (index, value) ->
            if (value == 0L) return@forEach
            val curr = sorted.find { it.index == index }!!
            val idx = sorted.indexOfFirst { it.index == index }
            sorted.removeAt(idx)
            val nexIdx = ((idx.toLong() + value) % (sorted.size)).let {
                if (it <= 0) {
                    sorted.size - it.absoluteValue
                } else it
            }
            sorted.add(nexIdx.absoluteValue.toInt(), curr)
            //            sorted.map { it.value }.log("n $n")
        }
        return sorted
    }
    fun part1(input: List<Long>) {
        val mixed = input.withIndex().toMutableList().let { mix(it) }
        val zIdx = mixed.indexOfFirst { it.value == 0L }
        val normalized = mixed.subList(zIdx, mixed.size) + mixed.subList(0, zIdx)
        listOf(1000, 2000, 3000).map { normalized[(it % normalized.size)] }.sumOf { it.value }
                .log("part1")
    }

    fun part2(input: List<Long>) {
        val mixed = input.map { it * 811589153 }.withIndex().toMutableList().let {
            (0..9).fold(it) {acc, i ->
               mix(acc).also { it.map { it.value }.log("i: $i")}
            }
        }
        //861096091333 low
        val zIdx = mixed.indexOfFirst { it.value == 0L }
        val normalized = mixed.subList(zIdx, mixed.size) + mixed.subList(0, zIdx)
        listOf(1000, 2000, 3000).map { normalized[(it % normalized.size)] }.log().sumOf { it.value }
                .log("part1")

    }

    @JvmStatic
    fun main(args: Array<String>) {
        val input = downloadAndReadInput("Day20", "\n").filter { it.isNotBlank() }
                .map { it.toLong() }
        part1(input)
        part2(input)
    }
}
