import java.io.File

fun main() {
    val input = File("src/main/resources/day09.txt").readLines()
    println(puzzleOne(input))
    println(puzzleTwo(input))
}

private fun puzzleOne(input: List<String>): Any {
    val map = input.map { line -> line.toList().map { it.digitToInt() } }
    var sum = 0
    for (y in map.indices) {
        for (x in map[y].indices) {
            val point = map[y][x]
            val top = if (y > 0) map[y - 1][x] else 10
            val bottom = if (y < map.lastIndex) map[y + 1][x] else 10
            val left = if (x > 0) map[y][x - 1] else 10
            val right = if (x < map[y].lastIndex) map[y][x + 1] else 10
            if (point < top && point < bottom && point < left && point < right) {
                sum += 1 + point
            }
        }
    }
    return sum
}

private fun puzzleTwo(input: List<String>): Any {
    val map = input.map { line -> line.toList().map { it.digitToInt() } }

    data class Point(val x: Int, val y: Int) {
        fun neighbours(): List<Point> = buildList {
            if (y > 0) add(Point(x, y - 1))
            if (y < map.lastIndex) add(Point(x, y + 1))
            if (x > 0) add(Point(x - 1, y))
            if (x < map[y].lastIndex) add(Point(x + 1, y))
        }

        fun value() = map[y][x]

        fun isLow() = neighbours().all { value() < it.value() }
    }

    val basins = mutableListOf<Int>()
    for (y in map.indices) {
        for (x in map[y].indices) {
            val point = Point(x, y)
            if (point.isLow()) {
                val basin = mutableSetOf<Point>()
                val queue = mutableListOf(point)
                while (queue.isNotEmpty()) {
                    val p = queue.removeFirst()
                    basin.add(p)
                    p.neighbours()
                        .filter { it.value() != 9 && it.value() > p.value() }
                        .filter { !basin.contains(it) }
                        .forEach { queue.add(it) }
                }
                basins.add(basin.size)
            }
        }
    }
    return basins.sorted().takeLast(3).reduce { a, b -> a * b }
}