import java.io.File

fun main() {
    val input = File("src/main/resources/day08.txt").readLines()
    println(puzzleOne(input))
    println(puzzleTwo(input))
}

private fun puzzleOne(input: List<String>): Any {
    return input.sumOf { line ->
        line.split(" | ").last()
            .split(' ').count { it.length in setOf(2, 4, 3, 7) }
    }
}

private fun puzzleTwo(input: List<String>): Any {
    fun deduce(signals: List<Set<Char>>): Map<Set<Char>, Int> {
        val result = mutableMapOf<Int, Set<Char>>()
        result[1] = signals.find { it.size == 2 }!!
        result[7] = signals.find { it.size == 3 }!!
        result[4] = signals.find { it.size == 4 }!!
        result[8] = signals.find { it.size == 7 }!!
        result[9] = signals.find { it.size == 6 && it.containsAll(result.getValue(4)) }!!
        result[6] = signals.find { it.size == 6 && !it.containsAll(result.getValue(7)) }!!
        result[2] = signals.find { it.size == 5 && (result.getValue(9) - it).size == 2 }!!
        result[3] = signals.find { it.size == 5 && it.containsAll(result.getValue(7)) }!!
        result[5] = signals.find { it.size == 5 && (result.getValue(6) - it).size == 1 }!!
        result[0] = signals.find { it.size == 6 && (result.getValue(5) + it).size == 7 }!!
        return result.entries.associate { (k, v) -> v to k }
    }

    return input.sumOf { line ->
        val (sample, data) = line.split(" | ")
        val key = deduce(sample.split(" ").map { it.toSet() })
        val (a, b, c, d) = data.split(" ").map { it.toSet() }.map { key.getValue(it) }
        a * 1000 + b * 100 + c * 10 + d
    }
}