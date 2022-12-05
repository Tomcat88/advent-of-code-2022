import java.util.Stack

fun main() {

    fun buildStacks(input: String): List<Stack<String>> {
        val rows = input.split("\n")
        val numbersRow = rows.indexOfFirst { it.startsWith(" 1 ") }
        val stacks = mutableListOf<Stack<String>>()
        val range = 0 until numbersRow
        range.reversed().map { i ->
            rows[i].chunked(4).map { it.trim().removeSurrounding("[", "]") }
                    .forEachIndexed { index, crate ->
                        if (crate.isBlank()) return@forEachIndexed
                        val stack = stacks.getOrNull(index)
                        if (stack == null) {
                            stacks.add(Stack<String>().apply { add(crate) })
                        } else {
                            stack.add(crate)
                        }
                    }
        }
        return stacks

    }

    fun part1(input: List<String>) {
        val stacks = buildStacks(input[0])
        input[1].split("\n").filter { it.isNotBlank() }.map {
            it.split(" ").mapNotNull { it.toIntOrNull() }.map { it - 1 }
        }.forEach { (e1, e2, e3) ->
            (0..e1).forEach { _ ->
                stacks[e2].pop().let { stacks[e3].add(it) }
            }
        }
        println(stacks.joinToString("") { it.pop() })
    }

    fun part2(input: List<String>) {
        val stacks = buildStacks(input[0])
        input[1].split("\n").filter { it.isNotBlank() }.map {
            it.split(" ")
                    .let { listOf(it[1].toInt(), it[3].toInt(), it[5].toInt()).map { it - 1 } }
        }
                .forEach { l ->
                    (0..l.first()).map { _ ->
                        stacks[l[1]].pop()
                    }.reversed().forEach { stacks[l[2]].add(it) }
                }
        println(stacks.joinToString("") { it.pop() })

    }

    val input = readInput("Day05", "\n\n")
    println(part1(input))
    println(part2(input))
}


