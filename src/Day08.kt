
fun main() {

    fun part1(input: List<List<Int>>) {
        input.withIndex().sumOf { (rowIdx, row) ->
            row.withIndex().count { (colIdx, col) ->
               row.splitOnIndex(colIdx).any { it.all { col > it } } ||
               input.column(colIdx).splitOnIndex(rowIdx).any { it.all { col > it } }
            }
         }.log("part1")
    }

    fun part2(input: List<List<Int>>) {
        input.flatMapIndexed { rowIdx, row ->
            row.mapIndexed { colIdx, col ->
                row.splitOnIndex(colIdx, true).map { it.view(col) }.reduce(Int::times) *
                input.column(colIdx).splitOnIndex(rowIdx, true).map { it.view(col) }.reduce(Int::times)
            }
        }.max().log("part2")
    }

    val input = downloadAndReadInput("Day08").map { it.map { it.digitToInt() }.toList() }

    part1(input)
    part2(input)
}

fun List<Int>.splitOnIndex(separatorIdx: Int, reverseFirst: Boolean = false) =
    listOf(subList(0, separatorIdx).let { if (reverseFirst) it.reversed() else it }, subList(separatorIdx + 1, size))
fun <T> List<List<T>>.column(i: Int): List<T> = map { it[i] }
fun List<Int>.view(v: Int) =
        indexOfFirst { it >= v }.takeIf { it != -1 }?.let { it + 1 } ?: size
