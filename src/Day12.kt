import java.util.Stack

object Day12 {
    @JvmStatic
    fun main(args: Array<String>) {

        fun part1(grid: List<List<GridLetter>>) {
            val visited = mutableSetOf<GridLetter>()
            val start = grid.column(0).first { it.c == 'S' }
            var currList = setOf(start)
            var step = 0
            while (true) {
                visited.addAll(currList)
                if (currList.any { it.c == 'z' + 1 }) {
                   break
                }
                currList = currList.flatMap { curr ->
                    setOfNotNull(curr.up, curr.down, curr.left, curr.right)
                        .filterNot { visited.contains(it) }
                        .filterIsInstance<GridLetter>().filterNot { it.c == 'S' }
                        .filter { nv -> curr.c == 'S' || nv.c - curr.c == 1 || curr.c >= nv.c }
                 }.toSet()
//                grid.forEach {
//                    it.map { if (visited.contains(it)) "█" else it.c }.forEach { print(it) }
//                    println()
//                }
                step++
            }
            step.log("part1")
        }

        fun part2(grid: List<List<GridLetter>>) {
            val visited = mutableSetOf<GridLetter>()
            val start = grid.flatMap { it.filter { it.c == 'a' } }
            var currList = start.toSet()
            var step = 0
            while (true) {
                visited.addAll(currList)
                if (currList.any { it.c == 'z' + 1 }) {
                    break
                }
                currList = currList.flatMap { curr ->
                    setOfNotNull(curr.up, curr.down, curr.left, curr.right)
                            .filterNot { visited.contains(it) }
                            .filterIsInstance<GridLetter>().filterNot { it.c == 'S' }
                            .filter { nv -> curr.c == 'S' || nv.c - curr.c == 1 || curr.c >= nv.c }
                }.toSet()
//                grid.forEach {
//                    it.map { if (visited.contains(it)) "█" else it.c }.forEach { print(it) }
//                    println()
//                }
                step++
            }
            step.log("part2")
        }

        val input = downloadAndReadInput("Day12", "\n").filter { it.isNotBlank() }.map { it.replace('E', 'z' + 1).toCharArray().toList()}
        val map = mutableMapOf<Point, GridLetter>()
        val grid = input.mapIndexed  { y, row ->
            row.mapIndexed { x, c ->
                val point = Point(x, y)
                val l = map[point] ?: GridLetter(c, point)
                map[point] = l
                listOf(Up, Down, Right, Left).map { move ->
                   if (point.canMove(move, input)) {
                       val moved = point.move(move)
                       val movedGridLetter: GridLetter = map[moved] ?: GridLetter(input.getPoint(moved), moved)
                       l.set(move, movedGridLetter)
                       map[moved] = movedGridLetter
                       movedGridLetter
                   }
                }
                l
             }
        }
//        grid.forEach {
//            it.forEach { print(it.c) }
//            println()
//         }
        part1(grid)
        part2(grid)
    }

    data class GridLetter(
        val c: Char, val p: Point,
        var up: GridLetter? = null,
        var down: GridLetter? = null,
        var left: GridLetter? = null,
       var right: GridLetter? = null) {
        fun set(mvmt: Move, l: GridLetter) {
            when(mvmt) {
                Down -> down = l
                Left -> left = l
                Right -> right = l
                Up -> up = l
            }
        }
        override fun toString(): String {
            return "GridLetter(c=$c, p=$p)"
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is GridLetter) return false

            if (c != other.c) return false
            if (p != other.p) return false

            return true
        }

        override fun hashCode(): Int {
            var result = c.hashCode()
            result = 31 * result + p.hashCode()
            return result
        }

    }

    private fun Grid.getPoint(p: Point): Char {
        return this[p.y][p.x]
    }
    data class Point(var x: Int, var y: Int) {
        fun canMove(mvmt: Move, grid: Grid): Boolean {
            return when (mvmt) {
                is Down -> y < grid.size -1
                is Left -> x >= 1
                is Right -> x < grid[0].size -1
                is Up -> y >= 1
            }
        }
        fun move(mvmt: Move): Point {
            return when(mvmt) {
                is Down -> Point(x,y + 1)
                is Left -> Point(x - 1, y)
                is Right -> Point(x + 1, y)
                is Up -> Point(x, y - 1)
            }
        }
    }

    sealed interface Move
    object Up : Move
    object Down : Move
    object Left : Move
    object Right : Move

}

typealias Grid = List<List<Char>>
