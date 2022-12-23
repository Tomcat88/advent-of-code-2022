import java.awt.geom.Point2D

object Day22 {

    val directions = listOf(
            1 to 0, // R
            0 to 1, // D
            -1 to 0, // L
            0 to -1, // U
    )

    fun parseBoard(board: String): Map<IntPair, Char> {
        return board.split("\n").flatMapIndexed { y, row ->
            row.toCharArray().mapIndexed { x, c ->
                when (c) {
                    ' ' -> null
                    '.', '#' -> (x to y) to c
                    else -> error("unknown board cell")
                }
            }
        }.filterNotNull().toMap()
    }

    fun parsePath(path: String): List<IntPair> {
        var acc = ""
        return path.map { c ->
            if (c.isDigit()) {
                acc += c
                null
            } else {
                val r = acc.toInt() to when (c) {
                    'R' -> 1
                    'L' -> -1
                    else -> error("unknown turning options")
                }
                acc = ""
                r
            }
        }.filterNotNull() + (acc.toInt() to 0)
    }

    private fun Map<IntPair, Char>.getNextPos(from: IntPair, dir: IntPair): IntPair? {
        return this[from + dir].let { v ->
            when (v) {
                '#' -> null
                '.' -> from + dir
                null -> {
                    if (dir.first == 1) {
                        this.filterKeys { it.second == from.second }.minBy { it.key.first }
                                .takeIf { it.value == '.' }?.key
                    } else if (dir.first == -1) {
                        this.filterKeys { it.second == from.second }.maxBy { it.key.first }
                                .takeIf { it.value == '.' }?.key
                    } else if (dir.second == 1) {
                        this.filterKeys { it.first == from.first }.minBy { it.key.second }
                                .takeIf { it.value == '.' }?.key
                    } else if (dir.second == -1) {
                        this.filterKeys { it.first == from.first }.maxBy { it.key.second }
                                .takeIf { it.value == '.' }?.key
                    } else error("unknown wrapping")
                }

                else -> error("unknown val")
            }
        }
    }

    fun part1(board: Map<IntPair, Char>, path: List<IntPair>) {
        var didx = 0
        var start = board.filter { e -> e.key.second == 0 && e.value == '.' }
                .minBy { it.key.first }.key
        var dir = directions[0].log()
        start.log()
        path.forEach { (times, rotation) ->
            var i = 0
            while (i < times) {
                val nextPos = board.getNextPos(start, dir) ?: break
                start = nextPos
                i++
            }
            didx += rotation
            if (didx < 0) didx = directions.size - 1
            dir = directions[didx % directions.size]
        }
        //144168 low
        //144169 low
        //144252 high
        start.log().let { (1000 * (it.second + 1)) + (4 * (it.first + 1)) }.log()
        dir.log()
    }

    fun part2(input: List<String>) {

    }

    @JvmStatic
    fun main(args: Array<String>) {
        val input = downloadAndReadInput("Day22", "\n\n").filter { it.isNotBlank() }
        val (boardStr, pathStr) = input
        val board = parseBoard(boardStr)
        val path = parsePath(pathStr.removeSuffix("\n")).log()
        part1(board, path)
    }
}
