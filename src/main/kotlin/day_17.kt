import java.io.File
import java.lang.Integer.max
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.sqrt

fun main() {
    val input = File("src/main/resources/day17.txt").readLines()
    println(puzzleOne(input))
    println(puzzleTwo(input))
}

private fun puzzleOne(input: List<String>): Any {
    val (xMin, xMax, yMin, yMax) = input.first().removePrefix("target area: ")
        .replace("x=", "")
        .replace("y=", "")
        .split("..", ", ")
        .map { it.toInt() }

    var maxHeight = 0
    for (xv in sqrt(2.0 * xMin).toInt()..xMax) {
        var x = 0
        for (k in 1..2 * xv) {
            x += max(0, xv - (k - 1))
            if (x > xMax) break
            if (x >= xMin) {
                val yvMin = ceil((2 * yMin + k * k - k) / 2.0 / k).toInt()
                val yvMax = floor((2 * yMax + k * k - k) / 2.0 / k).toInt()
                var yv = yvMin
                while (yv <= yvMax) {
                    maxHeight = max(maxHeight, (yv + 1) * yv / 2)
                    yv++
                }
            }
        }
    }

    return maxHeight
}

private fun puzzleTwo(input: List<String>): Any {
    val (xMin, xMax, yMin, yMax) = input.first().removePrefix("target area: ")
        .replace("x=", "")
        .replace("y=", "")
        .split("..", ", ")
        .map { it.toInt() }

    val pairs = mutableSetOf<Pair<Int, Int>>()
    for (xv in sqrt(2.0 * xMin).toInt() - 1..xMax) {
        var x = 0
        for (k in 1..xv * xv) {
            x += max(0, xv - (k - 1))
            if (x > xMax) break
            if (x >= xMin) {
                val yvMin = ceil((2 * yMin + k * k - k) / 2.0 / k).toInt()
                val yvMax = floor((2 * yMax + k * k - k) / 2.0 / k).toInt()
                var yv = yvMin
                while (yv <= yvMax) {
                    pairs.add(xv to yv)
                    yv++
                }
            }
        }
    }

    return pairs.size
}