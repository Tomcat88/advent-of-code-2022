import java.util.Stack

object Day13 {
    fun part1(input: List<List<Item>>) {
        input.mapIndexed { i, l ->
            val (first, second) = l
            first.compareTo(second).takeIf { it == -1 }?.let { i + 1 }
        }.filterNotNull().sum().log("part1")
    }

    fun part2(input: List<List<Item>>) {
        val sentinels = listOf(L(L(2)), L(L(6)))
        (input.flatten() + sentinels).asSequence().sortedWith(Item.ItemComparator).withIndex()
                .filter { (i, l) -> sentinels.any { s -> s.compareTo(l) == 0 } }
                .map { it.index + 1 }.reduce(Int::times).log("part2")
    }

    @JvmStatic
    fun main(args: Array<String>) {
        val input = downloadAndReadInput("Day13", "\n\n").filter { it.isNotBlank() }
                .map { l ->
                    l.split("\n").filter { it.isNotBlank() }.map { s ->
                        deserialize(s)
                    }
                }
        part1(input)
        part2(input)
    }

    private fun deserialize(s: String): L {
        val stack = Stack<L>()
        s.removeSurrounding("[", "]").let {
            stack.push(L())
            var acc = ""
            val addAccAndReset = { s: String ->
                if (s.isNotBlank()) {
                    s.toI().let { stack.peek().add(it) }
                    acc = ""
                }
            }
            it.forEach { c ->
                when (c) {
                    '[' -> stack.push(L())
                    ']' -> {
                        addAccAndReset(acc)
                        stack.pop().let { stack.peek().add(it) }
                    }

                    else -> {
                        if (c.isDigit()) acc += c
                        if (c == ',') {
                            addAccAndReset(acc)
                        }
                    }
                }
            }
            addAccAndReset(acc)
        }
        return stack.pop()
    }

    sealed interface Item : Comparable<Item> {

        object ItemComparator : Comparator<Item> {
            override fun compare(o1: Item, o2: Item): Int {
                return o1.compareTo(o2)
            }

        }
    }

    data class L(val list: MutableList<Item>) : Item {
        constructor(i: Int) : this(mutableListOf(I(i)))
        constructor(l: L) : this(mutableListOf(l))
        constructor() : this(mutableListOf())

        fun add(itm: Item) = list.add(itm)
        override fun compareTo(other: Item): Int {
            return when (other) {
                is I -> compareTo(L(other.item))
                is L -> {
                    var i = 0
                    var cmp = 0
                    while (i < list.size) {
                        val left = list[i]
                        val right = other.list.getOrNull(i)
                        if (right == null) {
                            cmp = 1
                            break
                        }
                        cmp = left.compareTo(right)
                        if (cmp != 0) {
                            break
                        }
                        i++
                    }
                    if (i == list.size && i < other.list.size) {
                        cmp = -1
                    }
                    return cmp
                }
            }
        }

        override fun toString(): String = "[${list.joinToString(",") { it.toString() }}]"
    }

    private fun String.toI() = this.toInt().let { I(it) }
    data class I(val item: Int) : Item {

        override fun compareTo(other: Item): Int {
            return when (other) {
                is I -> item.compareTo(other.item)
                is L -> L(item).compareTo(other)
            }
        }

        override fun toString(): String = "$item"
    }
}
