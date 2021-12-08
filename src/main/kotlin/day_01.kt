import java.io.File

fun main() {
    val input = File("src/main/resources/day01.txt").readLines()
    println(puzzleOne(input))
    println(puzzleTwo(input))
}

private fun puzzleOne(input: List<String>): Any {
    return input.map { it.toInt() }.zipWithNext { a, b -> if (a < b) 1 else 0 }.sum()
}

private fun puzzleTwo(input: List<String>): Any {
    val values = input.map { it.toInt() }
    return (0..values.size-3)
        .map { values[it] + values[it+1] + values[it+2] }
        .zipWithNext { a, b -> if (a < b) 1 else 0  }.sum()
}