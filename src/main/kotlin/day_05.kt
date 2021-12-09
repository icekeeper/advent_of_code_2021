import java.io.File
import java.lang.Integer.max
import java.lang.Integer.min

fun main() {
    val input = File("src/main/resources/day05.txt").readLines()
    println(puzzleOne(input))
    println(puzzleTwo(input))
}


private fun puzzleOne(input: List<String>): Any {
    val map = MutableList(1000) { MutableList(1000) { 0 } }
    input.forEach { line ->
        val (x1, y1, x2, y2) = line.split(",", " -> ").map { it.toInt() }
        if (x1 == x2) {
            for (i in min(y1, y2)..max(y1, y2)) {
                map[i][x1] += 1
            }
        } else if (y1 == y2) {
            for (i in min(x1, x2)..max(x1, x2)) {
                map[y1][i] += 1
            }
        }
    }

    return map.sumOf { it.count { n -> n > 1 } }
}

private fun puzzleTwo(input: List<String>): Any {
    val map = MutableList(1000) { MutableList(1000) { 0 } }
    input.forEach { line ->
        val (x1, y1, x2, y2) = line.split(",", " -> ").map { it.toInt() }
        if (x1 == x2) {
            for (y in min(y1, y2)..max(y1, y2)) {
                map[y][x1] += 1
            }
        } else if (y1 == y2) {
            for (x in min(x1, x2)..max(x1, x2)) {
                map[y1][x] += 1
            }
        } else {
            var x = x1
            var y = y1
            while (x != x2 && y != y2) {
                map[y][x] += 1
                x += if (x1 < x2) 1 else -1
                y += if (y1 < y2) 1 else -1
            }
            map[y][x] += 1
        }
    }

    return map.sumOf { it.count { n -> n > 1 } }
}