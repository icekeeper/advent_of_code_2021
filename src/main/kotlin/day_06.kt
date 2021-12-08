import java.io.File
import java.math.BigInteger

fun main(args: Array<String>) {
    val input = File("src/main/resources/day06.txt").readLines()
    println(puzzleOne(input))
    println(puzzleTwo(input))
}

private fun puzzleOne(input: List<String>): Any {
    return solve(input.first(), 80)
}

private fun puzzleTwo(input: List<String>): Any {
    return solve(input.first(), 256)
}

private fun solve(input: String, days: Int): BigInteger {
    val mem = mutableMapOf<Pair<Int, Int>, BigInteger>()
    fun star(c: Int, d: Int): BigInteger {
        if (c >= d) return BigInteger.ONE
        if (!mem.contains(c to d)) {
            mem[c to d] = if (c > 0) {
                star(0, d - c)
            } else {
                star(6, d - 1) + star(8, d - 1)
            }
        }
        return mem.getValue(c to d)
    }
    return input.split(",").map { it.toInt() }.sumOf { star(it, days) }
}
