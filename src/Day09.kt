import kotlin.math.abs

fun main() {

    fun part1(input: List<Mvmt>) {
        val head = Point(0, 0)
        val tail = Point(0, 0)
        val positions = mutableSetOf(tail.copy())
        input.forEach { mvmt ->
           head.move(mvmt)
           if (!tail.isAdjecent(head)) {
               tail.catchUp(head, mvmt)
               positions.add(tail.copy())
           }
        }
        positions.size.log("part1")

    }

    fun part2(input: List<Mvmt>) {
        val knots = (0..9).map { Point(0,0) }
        val positions = mutableSetOf(Point(0,0))
//        knots.plot(15, 15)
        input.forEach { mvmt ->
            knots[0].move(mvmt)
            knots.drop(1).forEachIndexed { i, p ->
                if (!p.isAdjecent(knots[i])) {
                    p.catchUp(knots[i], mvmt)
//                    knots.plot(35, 35)
                }
            }
//            knots.plot(35, 35, positions)
            positions.add(knots.last().copy())
        }
        positions.size.log("part2")
    }

    val input = downloadAndReadInput("Day09").filter { it.isNotBlank() }.flatMap { Mvmt.parse(it) }

    part1(input)
    part2(input)
}

fun List<Point>.plot(h : Int, w : Int, tailPositions: Collection<Point> = emptyList()) {
    (-(h/2)..(h/2)).reversed().forEach { y ->
        (-(w/2)..(w/2)).forEach { x ->
            val visited = tailPositions.contains(Point(x,y))
            val p  = indexOfFirst { it.x == x && it.y == y }.let {
                when (it) {
                    -1 -> if (y == 0 && x == 0)  "s" else if (visited) "#" else "."
                    0 -> "H"
                    size-1 -> "T"
                    else -> (it).toString()
                }
             }
            print(p)
         }
         println()
     }
}

sealed interface Mvmt {
    companion object {
        fun parse(s: String): List<Mvmt> {
            return s.split(" ").let { (m, t) ->
                (0 until t.toInt()).map {
                    when (m) {
                        "L" -> Left
                        "R" -> Right
                        "U" -> Up
                        "D" -> Down
                        else -> error("unknown")
                    }
                }
            }
        }
    }
}

object Up : Mvmt
object Down : Mvmt
object Left : Mvmt
object Right : Mvmt
data class Point(var x: Int, var y: Int) {

    fun isAdjecent(p: Point): Boolean {
        return abs(x - p.x) < 2 && abs(y - p.y) < 2
    }

    fun move(mvmt: Mvmt) {
        when (mvmt) {
            is Down -> y -= 1
            is Left -> x -= 1
            is Right -> x += 1
            is Up -> y += 1
        }
    }

    fun catchUp(p: Point, lastMvmt: Mvmt) {
        when (lastMvmt) {
            is Down -> {
                if (y != p.y) {
                    if (y < p.y) {
                        move(Up)
                    } else {
                        move(lastMvmt)
                    }
                }
                if (x < p.x) {
                    move(Right)
                } else if (x > p.x) {
                    move(Left)
                }
            }
            is Left -> {
                if (x != p.x) {
                    if (x < p.x) {
                        move(Right)
                    } else {
                        move(lastMvmt)
                    }
                }
                if (y < p.y) {
                    move(Up)
                } else if (y > p.y) {
                    move(Down)
                }
            }
            is Right -> {
                if (x != p.x) {
                    if (x > p.x) {
                        move(Left)
                    } else {
                        move(lastMvmt)
                    }
                }
                if (y < p.y) {
                    move(Up)
                } else if (y > p.y) {
                    move(Down)
                }
            }
            is Up -> {
                if (y != p.y) {
                    if (y > p.y) {
                        move(Down)
                    } else {
                        move(lastMvmt)
                    }
                }
                if (x < p.x) {
                    move(Right)
                } else if (x > p.x) {
                    move(Left)
                }
            }
        }
    }
}
