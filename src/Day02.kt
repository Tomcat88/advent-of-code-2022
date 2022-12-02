fun main() {

    fun part1(input: List<List<String>>): Int =
        input.sumOf {
            it[1].toRPS().round(it[0].toRPS()).total()
        }

    fun part2(input: List<List<String>>): Int =
        input.sumOf {
            it[0].toRPS().roundWithStrategy(it[1]).total()
        }

    val input = readInputAndSplit("Day02")
    println(part1(input))
    println(part2(input))
}

fun String.toRPS() = when(this) {
    "A", "X" -> Rock
    "B", "Y" -> Paper
    "C", "Z" -> Scissors
    else -> throw IllegalArgumentException("invalid value: $this")
}

sealed class RoundResult(private val result: Int, private val modifier: Int) {
    fun total() = result + modifier
}

class Win(result: Int) : RoundResult(result, 6)
class Draw(result: Int) : RoundResult(result, 3)
class Loss(result: Int) : RoundResult(result, 0)

sealed class RPS(private val value: Int) {
    abstract val winsAgainst: RPS
    abstract val losesAgainst: RPS

    fun round(you: RPS) = when (you) {
        this -> Draw(value)
        winsAgainst -> Win(value)
        else -> Loss(value)
    }
    fun roundWithStrategy(strategy: String) = when(strategy) {
        "Y" -> round(this)
        "Z" -> losesAgainst.round(this)
        "X" -> winsAgainst.round(this)
        else -> throw IllegalArgumentException("invalid strategy $strategy")
    }
}

object Rock : RPS(1) {
    override val winsAgainst  = Scissors
    override val losesAgainst = Paper
}

object Paper : RPS(2) {
    override val winsAgainst = Rock
    override val losesAgainst = Scissors
}

object Scissors : RPS(3) {
    override val winsAgainst = Paper
    override val losesAgainst = Rock
}


