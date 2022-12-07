import java.util.Stack
import java.util.function.Predicate

fun main() {

    val root = Dir("/")
    val ditStack = Stack<Dir>().apply { add(root) }
    fun part1() {
        root.filterSize {
            it <= 100000
        }.sum().log("part1")
    }

    fun part2() {
        val spaceLeft = 70000000 - root.size
        root.filterSize {
            it + spaceLeft > 30000000
        }.min().log("part2")
    }

    val input = readInput("Day07", "\n$").drop(1)
    input.forEach { cmdAndOutput ->
        val split = cmdAndOutput.trim().split("\n")
        val cmd = split.first().trim().split(" ")
        when (cmd.first()) {
            "cd" -> {
                if (cmd[1] == "..") ditStack.pop()
                else ditStack.push(ditStack.peek().getDir(cmd[1]))
            }

            "ls" -> ditStack.peek().apply { split.drop(1).forEach { n -> add(n) } }
        }
    }
    part1()
    part2()
}

sealed interface Node {
    val size: Long
}

data class Dir(val name: String, val nodes: MutableList<Node> = mutableListOf()) : Node {
    companion object {
        fun from(s: String) = Dir(s.removePrefix("dir "))
    }

    fun add(node: String) {
        if (node.startsWith("dir ")) {
            from(node)
        } else {
            File.from(node)
        }.let { nodes.add(it) }
    }

    fun getDir(name: String) = nodes.filterIsInstance<Dir>().first { n -> n.name == name }
    override val size: Long
        get() = nodes.sumOf { it.size }

    fun filterSize(predicate: Predicate<Long>): List<Long> {
        return nodes.filterIsInstance<Dir>()
                .map { it.size }
                .filter { predicate.test(it) }
                .toList() +
                nodes.filterIsInstance<Dir>()
                        .flatMap { it.filterSize(predicate) }
    }
}

data class File(val name: String, override val size: Long) : Node {
    companion object {
        fun from(s: String) = s.split(" ").let { File(it[1], it.first().toLong()) }
    }
}
