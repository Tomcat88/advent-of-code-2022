import java.awt.geom.Point2D

object Day23 {


    fun parseGrid(grid: List<String>): Map<IntPair, Boolean> {
        return grid.flatMapIndexed { y, row ->
            row.mapIndexedNotNull { x, c ->
                if (c == '#') (x to y) to true
                else null
            }
        }.toMap()
    }

    private fun IntPair.canMove(board: Map<IntPair, Boolean>): Boolean {
        return listOf(-1, 0, 1).flatMap { y ->
            listOf(-1, 0, 1).map { x -> x to y }
        }.filter { it != 0 to 0 }.map { it + this}.any { board.getOrDefault(it, false)}
    }
    private fun IntPair.planMove(board: Map<IntPair, Boolean>, dir: IntPair): IntPair? {
        return if (dir.first == 0) {
            listOf(-1, 0, 1).map { it to dir.second }.map { it + this }
                    .none { board.getOrDefault(it, false) }.takeIf { it }?.let { dir }
        } else if (dir.second == 0) {
            listOf(-1, 0, 1).map { dir.first to it }.map { it + this }
                    .none { board.getOrDefault(it, false) }.takeIf { it }?.let { dir }
        } else error("unknown dir")
    }

    private fun MutableMap<IntPair, Boolean>.round(directions: List<IntPair>): Boolean {
        return filterValues { it }
            .filterKeys { it.canMove(this)}
            .map { e -> e.key to directions.firstNotNullOfOrNull { dir -> e.key.planMove(this, dir) }
                     }
            .filter { it.second != null }
            .groupBy { it.first + it.second!! }
            .filterValues { e -> e.size == 1 }
            .mapValues { it.value.map { it.first }.first() }
            .map { (newPos, oldPos) ->
                this[newPos] = true
                this[oldPos] = false
                true
            }.any { it }
    }

    private fun MutableMap<IntPair, Boolean>.plot() {
        val minX = this.filterValues { it }.minOf { it.key.first }
        val minY = this.filterValues { it }.minOf { it.key.second }
        val maxX = this.filterValues { it }.maxOf { it.key.first }
        val maxY = this.filterValues { it }.maxOf { it.key.second }
        (minY..maxY).forEach { y -> (minX..maxX).forEach { x -> print(this.getOrDefault(x to y, false).let { if (it) "#" else "." }) }; println() }
    }

    fun part1(board: MutableMap<IntPair, Boolean>) {
        var directions = listOf(
                0 to -1, // North
                0 to 1, // South
                -1 to 0, // West
                1 to 0, // East
        )
        repeat(10_000) { n ->
            if (!board.round(directions)) {
                (n + 1).log("part2")
                return@repeat
            }
//            board.plot()
            directions = directions.drop(1) + directions.first()
//            directions.log()
            if (n == 9) {
                val minX = board.filterValues { it }.minOf { it.key.first }
                val minY = board.filterValues { it }.minOf { it.key.second }
                val maxX = board.filterValues { it }.maxOf { it.key.first }
                val maxY = board.filterValues { it }.maxOf { it.key.second }
                // 4026 too low
                (minY..maxY).flatMap { y -> (minX..maxX).map { x -> x to y } }
                        .map { board.getOrDefault(it, false) }.count { !it }.log("part1")
            }
        }
    }

    fun part2(input: List<String>) {

    }

    @JvmStatic
    fun main(args: Array<String>) {
        val input = downloadAndReadInput("Day23").filter { it.isNotBlank() }
        val grid = parseGrid(input).toMutableMap().log()
        part1(grid)
    }
}
