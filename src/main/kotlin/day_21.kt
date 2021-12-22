import java.io.File
import java.math.BigInteger
import kotlin.math.min

@ExperimentalStdlibApi
fun main() {
    val input = File("src/main/resources/day21.txt").readLines()
    println(puzzleOne(input))
    println(puzzleTwo(input))
}

private fun puzzleOne(input: List<String>): Any {
    var firstPos = input.first().takeLastWhile { it != ' ' }.toInt()
    var secondPos = input.last().takeLastWhile { it != ' ' }.toInt()

    var firstScore = 0
    var secondScore = 0
    var rolls = 0
    var firstMoves = true

    fun move(pos: Int, count: Int) = (pos - 1 + count) % 10 + 1
    fun roll() = rolls++ % 100 + 1

    while (firstScore < 1000 && secondScore < 1000) {
        if (firstMoves) {
            firstPos = move(firstPos, roll() + roll() + roll())
            firstScore += firstPos
        } else {
            secondPos = move(secondPos, roll() + roll() + roll())
            secondScore += secondPos
        }
        firstMoves = !firstMoves
    }

    return rolls * min(firstScore, secondScore)
}

@ExperimentalStdlibApi
private fun puzzleTwo(input: List<String>): Any {
    val firstPos = input.first().takeLastWhile { it != ' ' }.toInt()
    val secondPos = input.last().takeLastWhile { it != ' ' }.toInt()

    fun move(pos: Int, count: Int) = (pos - 1 + count) % 10 + 1

    data class State(val round: Int, val score: Int, val pos: Int) {
        fun next(roll: Int) = State(
            round = round + 1,
            score = score + move(pos, roll),
            pos = move(pos, roll),
        )
    }

    val rolls = buildMap<Int, Long> {
        for (i in 1..3)
            for (j in 1..3)
                for (k in 1..3)
                    merge(i + j + k, 1L, Long::plus)
    }.toList()

    fun computePossibilities(startPos: Int): Map<State, BigInteger> {
        val queue = mutableSetOf(State(0, 0, startPos))
        val result = mutableMapOf(State(0, 0, startPos) to BigInteger.ONE)
        while (queue.isNotEmpty()) {
            val state = queue.first()
            queue.remove(state)
            rolls.forEach { (roll, worlds) ->
                val next = state.next(roll)
                if (next.score < 21) {
                    queue.add(next)
                }
                result.merge(next, result.getValue(state) * worlds.toBigInteger(), BigInteger::plus)
            }
        }
        return result
    }

    val firstPossibilities = computePossibilities(firstPos)
    val secondPossibilities = computePossibilities(secondPos)

    val firstWins = firstPossibilities.entries.filter { it.key.score >= 21 }
        .sumOf { fp ->
            secondPossibilities.entries.filter { sp -> sp.key.round == fp.key.round - 1 && sp.key.score < 21 }
                .sumOf { sp -> sp.value * fp.value }
        }

    val secondWins = secondPossibilities.entries.filter { it.key.score >= 21 }
        .sumOf { sp ->
            firstPossibilities.entries.filter { fp -> fp.key.round == sp.key.round && fp.key.score < 21 }
                .sumOf { fp -> fp.value * sp.value }
        }

    return "first wins:$firstWins second wins:$secondWins max: ${if (firstWins > secondWins) "first" else "second"}"

}