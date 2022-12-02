fun main() {

    fun part1(input: List<String>): Int =
        input.map { RPS.parseRound(it) }.sumOf {
            it.first.round(it.second).total()
        }

    fun part2(input: List<String>): Int =
        input.map { RPS.parseStrategy(it) }.sumOf {
            it.first.roundWithStrategy(it.second).total()
        }

    val input = readInput("Day02")
    println(part1(input))
    println(part2(input))
}

sealed interface RoundResult {
    val result : Int
    val modifier : Int
    fun total() = result + modifier
}

class Win(override val result: Int) : RoundResult {
    override val modifier: Int = 6
}

class Draw(override val result: Int) : RoundResult {
    override val modifier: Int = 3
}

class Loss(override val result: Int) : RoundResult {
    override val modifier: Int = 0
}

sealed interface RPS {
    val value: Int
    fun round(you: RPS): RoundResult
    fun roundWithStrategy(strategy: String): RoundResult

    companion object {
        fun parseRound(round: String) = round.split(" ").let { Pair(parse(it[0]), parse(it[1])) }
        fun parseStrategy(round: String) = round.split(" ").let { Pair(parse(it[0]), it[1]) }

        private fun parse(string: String): RPS = when (string) {
            "A", "X" -> Rock()
            "B", "Y" -> Paper()
            "C", "Z" -> Scissors()
            else -> throw IllegalArgumentException("invalid value: $string")
        }
    }
}

class Rock : RPS {
    override val value : Int = 1
    override fun round(you: RPS): RoundResult = when(you) {
        is Rock -> Draw(you.value)
        is Paper -> Win(you.value)
        is Scissors -> Loss(you.value)
    }

    override fun roundWithStrategy(strategy: String): RoundResult = when(strategy) {
        "Y" -> round(Rock())
        "Z" -> round(Paper())
        "X" -> round(Scissors())
        else -> throw IllegalArgumentException("invalid strategy $strategy")
    }
}

class Paper : RPS {
    override val value : Int = 2
    override fun round(you: RPS): RoundResult = when(you) {
        is Paper -> Draw(you.value)
        is Scissors -> Win(you.value)
        is Rock -> Loss(you.value)
    }

    override fun roundWithStrategy(strategy: String): RoundResult = when(strategy) {
        "Y" -> round(Paper())
        "Z" -> round(Scissors())
        "X" -> round(Rock())
        else -> throw IllegalArgumentException("invalid strategy $strategy")
    }
}

class Scissors : RPS {
    override val value : Int = 3
    override fun round(you: RPS): RoundResult = when(you) {
        is Scissors -> Draw(you.value)
        is Rock -> Win(you.value)
        is Paper -> Loss(you.value)
    }

    override fun roundWithStrategy(strategy: String): RoundResult = when(strategy) {
        "Y" -> round(Scissors())
        "Z" -> round(Rock())
        "X" -> round(Paper())
        else -> throw IllegalArgumentException("invalid strategy $strategy")
    }
}


