import java.math.BigInteger

fun main() {

    fun part1(input: List<Monkey>) {
        repeat(20) { i ->
            input.forEach { m ->
                m.items.forEach { itm ->
                    m.testFn.invoke(m.op.invoke(itm) / BigInteger.valueOf(3))
                            .let { (newItm, newM) ->
                                input[newM].items.add(newItm)
                            }
                    m.inspections += 1
                }
                m.items.clear()
            }
        }
        input.sortedByDescending { it.inspections }.log().map { it.inspections }.take(2)
                .let { (f, s) -> f * s }.log("part1")
    }

    fun part2(input: List<Monkey>) {
        repeat(10_000) { i ->
            input.forEach { m ->
                m.items.forEach { itm ->
                    // Without this mod the solution will take an awful amount of time to complete
                    m.testFn.invoke((m.op.invoke(itm) % input.fold(BigInteger.ONE) { acc, m -> acc * m.divisibility }))
                            .let { (newItm, newM) ->
                                input[newM].items.add(newItm)
                            }
                    m.inspections += 1
                }
                m.items.clear()
            }
            if (i % 1000 == 0 || i == 20) {
                "Round $i".log()
                input.forEach { (it.index to it.inspections to it.items).log() }
            }
        }
        input.sortedByDescending { it.inspections }.map { it.inspections }.take(2)
                .let { (f, s) -> f * s }.log("part2")

    }

    downloadInput("11")
    val monkeys = readInput("Day11", "\n\n").map { it.split("\n").toMonkey() }
    val orignalItems = monkeys.map { it.items.map { it.longValueExact() } }
    part1(monkeys)
    monkeys.forEachIndexed { i, m -> m.reset(orignalItems[i]) }
    part2(monkeys)
}

fun List<String>.toMonkey(): Monkey {
    val idx = first().trim().removeSurrounding("Monkey ", ":").toInt()
    val items = get(1).trim().removePrefix("Starting items: ").split(", ").map { it.toBigInteger() }
    val op = { old: BigInteger ->
        val rightOp = get(2).trim().removePrefix("Operation: new = old ")
        val op: (BigInteger, BigInteger) -> BigInteger = when (rightOp.first()) {
            '+' -> BigInteger::plus
            '-' -> BigInteger::minus
            '/' -> BigInteger::div
            '*' -> BigInteger::times
            else -> error("unknown")
        }
        val rightVal = rightOp.drop(2).let {
            if (it == "old") old else it.toBigInteger()
        }
        op.invoke(old, rightVal)
    }
    val divisibility = get(3).trim().removePrefix("Test: divisible by ").toBigInteger()
    val testFn: (BigInteger) -> Pair<BigInteger, Int> = { item ->
        if (item.mod(divisibility) == BigInteger.ZERO) {
            get(4).trim().removePrefix("If true: throw to monkey ").toInt().let { item to it }
        } else {
            get(5).trim().removePrefix("If false: throw to monkey ").toInt().let { item to it }
        }
    }
    return Monkey(idx, op, testFn, items.toMutableList(), divisibility, 0)
}

data class Monkey(
        val index: Int,
        val op: (BigInteger) -> BigInteger,
        val testFn: (BigInteger) -> Pair<BigInteger, Int>,
        val items: MutableList<BigInteger>,
        val divisibility: BigInteger,
        var inspections: Long
) {
    fun reset(original: List<Long>) {
        inspections = 0
        items.clear()
        original.map { it.toBigInteger() }.forEach { items.add(it) }
    }
}
