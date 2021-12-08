import java.io.File
import kotlin.math.abs

fun main(args: Array<String>) {
    val input = File("src/main/resources/day07.txt").readLines()
    println(puzzleOne(input))
    println(puzzleTwo(input))
}

private fun puzzleOne(input: List<String>): Any {
    val positions = input.first().split(',').map { it.toInt() }.sorted().toList()
    val target = (positions[positions.size / 2 - 1] + positions[positions.size / 2]) / 2
    return positions.sumOf { abs(target - it) }
}

private fun puzzleTwo(input: List<String>): Any? {
    val positions = input.first().split(',').map { it.toInt() }.sorted().toList()
    fun fuel(x1:Int, x2:Int) = abs(x1-x2) * (1L + abs(x1-x2)) / 2
    fun sumFuel(x:Int) = positions.sumOf { fuel(it, x) }
    return (positions.first()..positions.last()).minOfOrNull { sumFuel(it) }
}