import java.io.File
import java.lang.Integer.min

@ExperimentalStdlibApi
fun main() {
    val input = File("src/main/resources/day15.txt").readLines()
    println(puzzleOne(input))
    println(puzzleTwo(input))
}

@ExperimentalStdlibApi
private fun puzzleOne(input: List<String>): Any {
    val map = input.map { it.map(Char::digitToInt) }
    val risk = List(map.size) { index -> MutableList(map[index].size) { Integer.MAX_VALUE } }
    val final = List(map.size) { index -> MutableList(map[index].size) { false } }
    val queue = mutableSetOf(0 to 0)
    risk[0][0] = 0

    fun neighbours(pair: Pair<Int, Int>) = buildList {
        val (y, x) = pair
        if (y > map.indices.first) add(y - 1 to x)
        if (y < map.indices.last) add(y + 1 to x)
        if (x > map[y].indices.first) add(y to x - 1)
        if (x < map[y].indices.last) add(y to x + 1)
    }

    while (queue.isNotEmpty()) {
        val m = queue.minByOrNull { risk[it.first][it.second] }!!
        queue.remove(m)
        final[m.first][m.second] = true
        neighbours(m).forEach { (y, x) ->
            if (!final[y][x]) {
                risk[y][x] = min(risk[y][x], risk[m.first][m.second] + map[y][x])
                queue.add(y to x)
            }
        }
    }

    return risk.last().last()
}

@ExperimentalStdlibApi
private fun puzzleTwo(input: List<String>): Any {
    val mapTile = input.map { it.map(Char::digitToInt) }
    val map = List(mapTile.size * 5) { y ->
        val ty = y % mapTile.size
        List(mapTile[ty].size * 5) { x ->
            val tx = x % mapTile[ty].size
            ((mapTile[ty][tx] - 1) + (y / mapTile.size) + (x / mapTile[ty].size)) % 9 + 1
        }
    }

    val risk = List(map.size) { index -> MutableList(map[index].size) { Integer.MAX_VALUE } }
    val final = List(map.size) { index -> MutableList(map[index].size) { false } }
    val queue = mutableSetOf(0 to 0)
    risk[0][0] = 0

    fun neighbours(pair: Pair<Int, Int>) = buildList {
        val (y, x) = pair
        if (y > map.indices.first) add(y - 1 to x)
        if (y < map.indices.last) add(y + 1 to x)
        if (x > map[y].indices.first) add(y to x - 1)
        if (x < map[y].indices.last) add(y to x + 1)
    }

    while (queue.isNotEmpty()) {
        val m = queue.minByOrNull { risk[it.first][it.second] }!!
        queue.remove(m)
        final[m.first][m.second] = true
        neighbours(m).forEach { (y, x) ->
            if (!final[y][x]) {
                risk[y][x] = min(risk[y][x], risk[m.first][m.second] + map[y][x])
                queue.add(y to x)
            }
        }
    }

    return risk.last().last()
}