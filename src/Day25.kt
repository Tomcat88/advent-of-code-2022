import kotlin.math.pow

object Day25 {


    fun part1(input: List<String>) {
        val number = input.sumOf { snafu ->
            snafu.fromSNAFU()
        }.log("from")
        number.toSNAFU().log("part1")

        // Not so elegant first solution below
//        var snafu = "20=022=200=========-"
//        var i = snafu.fromSNAFU().log("from")
//        (number - i).log("missing")
//        while (i < number) {
//            snafu = snafu.plus1SNAFU()
//            i++
//        }
//        snafu.log()
    }

    private fun String.fromSNAFU(): Long = reversed()
            .mapIndexed { i, n -> 5.0.pow(i) * n.fromSNAFU() }
            .sum()
            .toLong()

    private fun Long.toSNAFU() = generateSequence(this) {
        (it + 2) / 5
    }.takeWhile { it != 0L }
            .map { "012=-"[(it % 5).toInt()] }
            .joinToString("")
            .reversed()

    private fun String.plus1SNAFU(): String {
        return if (length == 1) {
            first().plus1SNAFU().let { c -> if (c == '=') "1$c" else c.toString() }
        } else {
            last().plus1SNAFU().let { c ->
                if (c == '=') {
                    dropLast(1).let { it.plus1SNAFU() } + c
                } else {
                    dropLast(1) + c
                }
            }
        }
    }

    private fun Char.plus1SNAFU(): Char {
        return when (this) {
            '0' -> '1'
            '1' -> '2'
            '2' -> '='
            '=' -> '-'
            '-' -> '0'
            else -> error("unknown")
        }
    }

    private fun Char.fromSNAFU(): Int {
        return when (this) {
            '=' -> -2
            '-' -> -1
            else -> this.digitToInt()
        }
    }

    fun part2(input: List<String>) {

    }


    @JvmStatic
    fun main(args: Array<String>) {
        val input = downloadAndReadInput("Day25").filter { it.isNotBlank() }
        part1(input)
    }

}
