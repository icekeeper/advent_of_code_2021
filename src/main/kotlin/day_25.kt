import java.io.File

fun main() {
    val input = File("src/main/resources/day25.txt").readLines()
    println(puzzleOne(input))
}

private fun puzzleOne(input: List<String>): Any {
    val map = input.map { it.toList() }

    fun moveEast(map: List<List<Char>>) = List(map.size) { y ->
        List(map[y].size) { x ->
            when (map[y][x]) {
                '.' -> when {
                    map[y][(x + map[y].size - 1) % map[y].size] == '>' -> '>'
                    else -> '.'
                }
                '>' -> when {
                    map[y][(x + 1) % map[y].size] == '.' -> '.'
                    else -> '>'
                }
                else -> map[y][x]
            }
        }
    }

    fun moveSouth(map: List<List<Char>>) = List(map.size) { y ->
        List(map[y].size) { x ->
            when (map[y][x]) {
                '.' -> when {
                    map[(y + map.size - 1) % map.size][x] == 'v' -> 'v'
                    else -> '.'
                }
                'v' -> when {
                    map[(y + 1) % map.size][x] == '.' -> '.'
                    else -> 'v'
                }
                else -> map[y][x]
            }
        }
    }

    fun move(map: List<List<Char>>) = moveSouth(moveEast(map))

    var count = 1
    var prev = map
    var next = move(map)
    while (next != prev) {
        count++
        prev = next
        next = move(next)
    }

    return count
}