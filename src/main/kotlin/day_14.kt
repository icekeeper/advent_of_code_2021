import java.io.File

fun main() {
    val input = File("src/main/resources/day14.txt").readLines()
    println(puzzleOne(input))
    println(puzzleTwo(input))
}

private fun puzzleOne(input: List<String>): Any {
    val template = input.first()
    val rules = input.drop(2).map { it.split(" -> ") }.associate { (a, b) -> a to b.first() }
    fun insert(t: String) = t.windowed(2, partialWindows = true)
        .flatMap { rules[it]?.let { c -> listOf(it.first(), c) } ?: listOf(it.first()) }.joinToString("")

    val result = generateSequence(template, ::insert).elementAt(10)
    val frequency = result.groupingBy { it }.eachCount()
    return frequency.maxOf { it.value } - frequency.minOf { it.value }
}

private fun puzzleTwo(input: List<String>): Any {
    var pairs = input.first().windowed(2).groupingBy { it }.eachCount()
        .mapValues { it.value.toLong() }
    val frequency = input.first().groupingBy { it }.eachCount()
        .mapValues { it.value.toLong() }.toMutableMap()
    val rules = input.drop(2).map { it.split(" -> ") }
        .associate { (a, b) -> a to b }

    repeat(40) {
        val nextPairs = mutableMapOf<String, Long>()
        pairs.forEach { e ->
            val rule = rules[e.key]
            if (rule != null) {
                nextPairs.merge(e.key.first() + rule, e.value, Long::plus)
                nextPairs.merge(rule + e.key.last(), e.value, Long::plus)
                frequency.merge(rule.first(), e.value, Long::plus)
            } else {
                nextPairs.merge(e.key, e.value, Long::plus)
            }
        }
        pairs = nextPairs
    }

    return frequency.maxOf { it.value } - frequency.minOf { it.value }
}