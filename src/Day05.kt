import java.util.Stack

fun main() {

    fun buildStacks(input: String): List<Stack<String>> {
        val rows = input.split("\n")
        val stacks = MutableList<Stack<String>>(rows.last().split(" \\d ".toRegex()).size) {
            Stack()
        }
        rows.reversed().drop(1).map { row ->
            row.chunked(4).map { it[1].toString() } // [A] [B]     [C] .....
        }.forEach {
            it.forEachIndexed { index, crate ->
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

    fun getMoves(input: List<String>) = input[1].split("\n").filter { it.isNotBlank() }.map {
        it.split(" ").mapNotNull { it.toIntOrNull() }.map { it - 1 }
    }

    fun part1(input: List<String>) {
        val stacks = buildStacks(input[0])
        getMoves(input).forEach { (e1, e2, e3) ->
            repeat(e1 + 1) { _ ->
                stacks[e2].pop().let { stacks[e3].add(it) }
            }
        }
        stacks.joinToString("") { it.pop() }.log("part1")
    }

    fun part2(input: List<String>) {
        val stacks = buildStacks(input[0])
        getMoves(input).forEach { (e1, e2, e3) ->
            (0..e1).map { _ ->
                stacks[e2].pop()
            }.reversed().let { stacks[e3].addAll(it) }
        }
        stacks.joinToString("") { it.pop() }.log("part2")
    }

    val input = readInput("Day05", "\n\n")
    part1(input)
    part2(input)
}


