import java.io.File

fun main() {
    val input = File("src/main/resources/day11.txt").readLines()
    println(puzzleOne(input))
    println(puzzleTwo(input))
}

private fun puzzleOne(input: List<String>): Any {
    val map = input.map { it.map { c -> c.digitToInt() }.toMutableList() }
    var counter = 0
    fun flush(x: Int, y: Int) {
        counter++
        for (y1 in y - 1..y + 1) {
            if (y1 in map.indices) {
                for (x1 in x - 1..x + 1) {
                    if (x1 in map.indices) {
                        if (++map[y1][x1] == 10) {
                            flush(x1, y1)
                        }
                    }
                }
            }
        }
    }

    fun step() {
        for (y in map.indices) {
            for (x in map[y].indices) {
                if (++map[y][x] == 10) {
                    flush(x, y)
                }
            }
        }
        for (y in map.indices) {
            for (x in map[y].indices) {
                if (map[y][x] > 9) {
                    map[y][x] = 0
                }
            }
        }
    }
    repeat(100) { step() }
    return counter
}

private fun puzzleTwo(input: List<String>): Any {
    val map = input.map { it.map { c -> c.digitToInt() }.toMutableList() }
    var counter = 0
    fun flush(x: Int, y: Int) {
        counter++
        for (y1 in y - 1..y + 1) {
            if (y1 in map.indices) {
                for (x1 in x - 1..x + 1) {
                    if (x1 in map.indices) {
                        if (++map[y1][x1] == 10) {
                            flush(x1, y1)
                        }
                    }
                }
            }
        }
    }

    fun step() {
        for (y in map.indices) {
            for (x in map[y].indices) {
                if (++map[y][x] == 10) {
                    flush(x, y)
                }
            }
        }
        for (y in map.indices) {
            for (x in map[y].indices) {
                if (map[y][x] > 9) {
                    map[y][x] = 0
                }
            }
        }
    }

    var stepCounter = 0L
    while (counter != 100) {
        counter = 0
        step()
        stepCounter++
    }
    return stepCounter
}