object Day21 {

    var monkeys = mutableMapOf<String, () -> (Long?)>()
    fun resolve(m1: String, m2: String): Pair<Long, Long>? {
        return listOf(m1, m2).map { monkeys[it] }.mapNotNull { it?.invoke() }.takeIf {
            it.size == 2
        }?.let { Pair(it.first(), it.component2()) }
    }

    fun part1(input: List<String>) {
        input.map { row ->
            row.split(":").let { (name, op) ->
                val f: (() -> Long?) = op.trim().toLongOrNull()?.let { { it } }
                        ?: op.trim().split(" ").let { expr ->
                            expr.let { (lh, o, rh) ->
                                when (o) {
                                    "+" -> {
                                        {
                                            resolve(lh, rh)?.let { (l, r) -> l + r }
                                        }
                                    }

                                    "-" -> {
                                        { resolve(lh, rh)?.let { (l, r) -> l - r } }
                                    }

                                    "*" -> {
                                        { resolve(lh, rh)?.let { (l, r) -> l * r } }
                                    }

                                    "/" -> {
                                        { resolve(lh, rh)?.let { (l, r) -> l / r } }
                                    }

                                    else -> error("op unknown")
                                }
                            }
                        }
                monkeys[name] = f
            }
        }
        monkeys["root"]?.invoke().log()
    }

    fun part2(input: List<String>) {
        input.map { row ->
            row.split(":").let { (name, op) ->
                val f: (() -> Long?) = op.trim().toLongOrNull()?.let { { it } }
                        ?: op.trim().split(" ").let { expr ->
                            expr.let { (lh, o, rh) ->
                                when (o) {
                                    "+" -> {
                                        {
                                            resolve(lh, rh)?.let { (l, r) -> l + r }
                                        }
                                    }

                                    "-" -> {
                                        { resolve(lh, rh)?.let { (l, r) -> l - r } }
                                    }

                                    "*" -> {
                                        { resolve(lh, rh)?.let { (l, r) -> l * r } }
                                    }

                                    "/" -> {
                                        { resolve(lh, rh)?.let { (l, r) -> l / r } }
                                    }

                                    "=" -> {
                                        {
                                            resolve(lh, rh)
//                                                    ?.also { (l, r) -> (l - r).log() } // An elegant solution
                                                    ?.let { (l, r) -> if (l == r) 0L else 1 }
                                        }
                                    }

                                    else -> error("op unknown")
                                }
                            }
                        }
                monkeys[name] = f
            }
        }
        var human = 3_378_273_350_000L
        monkeys["rpjv"] = { 109255486022220 }
        while (true) {
            monkeys["humn"] = { human }
            if (monkeys["root"]?.invoke()?.let { it == 0L } == true) break
            human++
        }
        human.log()
    }

    @JvmStatic
    fun main(args: Array<String>) {
        val input = downloadAndReadInput("Day21", "\n").filter { it.isNotBlank() }
        //        part1(input)
        part2(input.map { if (it.startsWith("root")) it.replace("+", "=") else it }
                      .map { if (it.startsWith("humn")) "humn: 0" else it })
    }
}
