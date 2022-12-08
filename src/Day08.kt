
fun main() {

    fun part1(input: List<List<Int>>) {
        input.withIndex().sumOf { row ->
            row.value.withIndex().count { col ->
               row.value.splitOnIndex(col.index).any { it.all { col.value > it } } ||
               input.column(col.index).splitOnIndex(row.index).any { it.all { col.value > it } }
            }
         }.log("part1")
    }

    fun part2(input: List<List<Int>>) {
        input.flatMapIndexed { rowIdx, row ->
            row.mapIndexed { colIdx, col ->
                row.splitOnIndex(colIdx, true).map { it.view(col) }.reduce { acc, i -> acc * i } *
                input.column(colIdx).splitOnIndex(rowIdx, true).map { it.view(col) }.reduce {acc, i -> acc * i  }
            }
        }.max().log("part2")
    }

    val input = downloadAndReadInput("Day08").map { it.map { it.digitToInt() }.toList() }

    part1(input)
    part2(input)
}

fun List<Int>.splitOnIndex(separator: Int, reverseFirst: Boolean = false) =
    listOf(subList(0, separator).let { if (reverseFirst) it.reversed() else it }, subList(separator + 1, size))
fun List<List<Int>>.column(i: Int): List<Int> = map { it[i] }
fun List<Int>.view(v: Int) =
        indexOfFirst { it >= v }.takeIf { it != -1 }?.let { it + 1 } ?: size
