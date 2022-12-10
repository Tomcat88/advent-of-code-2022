fun main() {

    fun part1(input: List<Cmd>) {
        var x = 1
        input.mapIndexedNotNull { i, cmd ->
            var ret: Int? = null
            if (i + 1 in listOf(20, 60, 100, 140, 180, 220)) {
                ret = x * (i + 1)
            }
            when (cmd) {
                is Addx -> x += cmd.value
                Noop -> {}
            }
            ret
        }.sum().log("part1")
    }

    fun part2(input: List<Cmd>) {
        var x = 1
        var sprite = 0 until 3
        input.forEachIndexed { i, cmd ->
            if (i % 40 in sprite) {
                print("#")
            } else {
                print(".")
            }
            when (cmd) {
                is Addx -> {
                    x += cmd.value
                    sprite = (x - 1) until (x + 2)
                }
                Noop -> {}
            }
            if ((i + 1) % 40 == 0) {
                println()
            }
        }
        "part2".log()
    }

    val input = downloadAndReadInput("Day10").flatMap { Cmd.parse(it) }

    part1(input)
    part2(input)
}

sealed interface Cmd {
    companion object {
        fun parse(s: String): List<Cmd> {
            return if (s == "noop") listOf(Noop)
            else if (s.startsWith("addx")) listOf(
                    Noop, Addx(
                    s.removePrefix("addx ").toInt()))
            else error("unknown")
        }
    }
}

object Noop : Cmd

data class Addx(val value: Int) : Cmd
