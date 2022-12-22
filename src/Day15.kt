import kotlin.math.abs

object Day15 {

    fun part1(input: List<Pair<Pair<Int, Int>, Pair<Int, Int>>>) {
        val grid = mutableMapOf<Pair<Int, Int>, Char>()
        val rowToInspect = 2000000
        input.forEach { (sensor, beacon) ->
            sensor.log("s")
            beacon.log("b")
            val (sX, sY) = sensor
            val (bX, bY) = beacon
            grid[sensor] = 'S'
            grid[beacon] = 'B'
            val dx = abs(sX - bX)
            val dy = abs(sY - bY)
            val radius = dx + dy
            val diff = abs(rowToInspect - sY)
            (sX - (radius - diff)..(sX + (radius - diff))).log()
                    .forEach { grid[it to rowToInspect] = '#' }
        }
        grid.filter { it.key.second == rowToInspect }.size.log()
    }

    fun part2(input: List<Sensor>) {
        val reducedMax = 4_000_000
        (0..reducedMax).forEach { row ->
            var col = 0
            while (col <= reducedMax) {
                val sb = input.find { (radius, sensor, beacon) ->
                    val (sX, sY) = sensor
                    val (bX, bY) = beacon
                    val dR = abs(row - sY)
                    val dC = abs(col - sX)
                    return@find dR + dC <= radius
                }
                if (sb != null) {
                    val (sX, sY) =  sb.center
                    val dR = abs(row - sY)
                    val dC = sX - col
                    col += (sb.radius - dR + dC) + 1
                } else {
                    (row to col).log("part2")
                    (col.toLong() * reducedMax.toLong() + row.toLong()).log("part2")
                    return@forEach
                }
            }
        }
        //        input.forEach { (sensor, beacon) ->
        //            sensor.log("s")
        //            beacon.log("b")
        //            val (sX, sY) = sensor
        //            val (bX, bY) = beacon
        //            grid[sensor] = 'S'
        //            grid[beacon] = 'B'
        //            val dx = abs(sX - bX)
        //            val dy = abs(sY - bY)
        //            val radius = dx + dy
        ////            (sY - radius..sY + radius).log().contains(rowToInspect).log("contains")
        //            ((sY - radius).. (sY + radius)).filter { it in 0..reducedMax }.forEach{ row ->
        //                val diff = abs(row - sY)
        //                (sX - (radius - diff)..(sX + (radius - diff))).filter { it >= 0 && it <= reducedMax }.forEach { grid[it to row] = '#' }
        //            }
        //        }
        //        grid.size.log("part2")
        //        grid.filter{ it.key.second == rowToInspect }.size.log()
    }

    @JvmStatic
    fun main(args: Array<String>) {
        val input = downloadAndReadInput("Day15", "\n").filter { it.isNotBlank() }.map {
            it.split(":").let { (sensor, beacon) ->
                sensor.removePrefix("Sensor at ").split(",").let {
                    it[0].removePrefix("x=").toInt() to it[1].removePrefix(" y=").toInt()
                } to
                        beacon.removePrefix(" closest beacon is at ").split(",").let {
                            it[0].removePrefix("x=").toInt() to it[1].removePrefix(" y=").toInt()
                        }
            }.let { Sensor.of(it.first, it.second) }
        }.log()
        part2(input)
    }

    data class Sensor(val radius: Int, val center: Pair<Int, Int>, val beacon: Pair<Int, Int>) {
        fun inside(pair: Pair<Int, Int>): Boolean {
            val (x, y) = center
            val dR = abs(pair.second - y)
            val dC = abs(pair.first - x)
            return dR + dC <= radius
        }

        companion object {
            fun of(center: Pair<Int, Int>, beacon: Pair<Int, Int>): Sensor {
                val (sX, sY) = center
                val (bX, bY) = beacon
                val dx = abs(sX - bX)
                val dy = abs(sY - bY)
                return Sensor(dx + dy, center, beacon)
            }
        }
    }

}

operator fun Pair<Int, Int>.plus(pair: Pair<Int, Int>) = (first + pair.first) to (second + pair.second)
