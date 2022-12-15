import kotlin.math.abs

object Day14 {
    @JvmStatic
    fun main(args: Array<String>) {
        val input = downloadAndReadInput("Day14", "\n").filter { it.isNotBlank() }.map {
            it.split(" -> ").map { it.split(",").let { (x, y) -> Point(x.toInt(), y.toInt()) } }
        }

        val minX = input.flatten().minOf { it.x } - 350
        val maxX = input.flatten().maxOf { it.x } + 200
        val minY = 0
        val maxY = input.flatten().maxOf { it.y } + 2
        minX.log()
        maxX.log()
        val xLength = maxX - minX
        val yLength = maxY - minY
        val grid = (0..yLength).map { y ->
            (0..xLength).map { x -> Field.Empty as Field }.toMutableList()
        }.toMutableList()
        input.forEach { rockPath ->
            rockPath.map { Point(it.x - minX, it.y - minY) }.windowed(2).forEach { (start, end) ->
                grid[start.y][start.x] = Field.Rock
                if (start.x == end.x) {
                    repeat(abs(start.y - end.y)) { i ->
                        if (start.y < end.y) {
                            grid[start.y + i + 1][start.x] = Field.Rock
                        } else {
                            grid[start.y - (i + 1)][start.x] = Field.Rock
                        }
                    }
                } else if (end.y == start.y) {
                    repeat(abs(start.x - end.x)) { i ->
                        if (start.x < end.x) {
                            grid[start.y][start.x + i + 1] = Field.Rock
                        } else {
                            grid[start.y][start.x - (i + 1)] = Field.Rock
                        }
                    }

                }
            }
        }
        grid[grid.size - 1] = (0..xLength).map { x -> Field.Rock as Field }.toMutableList()
        var i = 0
        var exit = false
        var part1 = false
        while (i < 30000 && !exit) {
            var s = Point(500 - minX, 0)
            var atRest = false
            while (!atRest) {
                when {
                    !part1 && (s.y + 1 >= grid.size - 1 || s.x - 1 < 0) -> {
                        part1 = true
                        i.log("part1")
                    }

                    grid[s.y + 1][s.x] is Field.Empty -> {
                        s = Point(s.x, s.y + 1)
                    }

                    grid[s.y + 1][s.x] is Field.NotEmpty
                            && grid[s.y + 1][s.x - 1] is Field.Empty -> {
                        s = Point(s.x - 1, s.y + 1)
                    }

                    grid[s.y + 1][s.x] is Field.NotEmpty
                            && grid[s.y + 1][s.x + 1] is Field.Empty -> {
                        s = Point(s.x + 1, s.y + 1)
                    }

                    else -> {
                        atRest = true
                        grid[s.y][s.x] = Field.Sand
                        if (s.y == 0) {
                            (i + 1).log("part2")
                            exit = true
                            break
                        }
                    }
                }
            }
            i++
        }
        //        plot(grid)
    }

    fun plot(grid: MutableList<MutableList<Field>>) {
        grid.forEachIndexed { x, row ->
            row.forEachIndexed { y, f ->
                when (f) {
                    is Field.Empty -> "."
                    is Field.Rock -> "#"
                    is Field.Sand -> "o"
                }.let { print(it) }
            }
            println()
        }
    }

    data class Point(val x: Int, val y: Int)

    sealed interface Field {
        sealed interface NotEmpty : Field
        object Rock : NotEmpty
        object Sand : NotEmpty
        object Empty : Field
    }

}
