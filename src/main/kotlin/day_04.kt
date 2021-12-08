import java.io.File

fun main() {
    val input = File("src/main/resources/day04.txt").readLines()
    println(puzzleOne(input))
    println(puzzleTwo(input))
}

private data class Board(val rows: List<MutableSet<Int>>, val columns: List<MutableSet<Int>>) {
    fun hasWon(): Boolean = rows.any { it.isEmpty() } || columns.any { it.isEmpty() }
    fun call(n: Int) {
        rows.forEach { it.remove(n) }
        columns.forEach { it.remove(n) }
    }
}

private fun puzzleOne(input: List<String>): Any {
    val numbers = input[0].split(",").map { it.toInt() }
    val boards = mutableListOf<Board>()

    input.subList(2, input.size).chunked(6).map { lines ->
        val columns = List(5) { mutableSetOf<Int>() }
        val rows = mutableListOf<MutableSet<Int>>()
        lines.subList(0, 5).forEach { line ->
            val lineNumbers = line.split(" ").filter { it.isNotBlank() }.map { it.toInt() }
            lineNumbers.forEachIndexed { index, n ->
                columns[index].add(n)
            }
            rows.add(lineNumbers.toMutableSet())
        }
        boards.add(Board(rows, columns))
    }

    numbers.forEach { n ->
        boards.forEach { board ->
            board.call(n)
            if (board.hasWon()) {
                return 1L * n * board.columns.sumOf { it.sum() }
            }
        }
    }

    return ""
}

private fun puzzleTwo(input: List<String>): Any {
    val numbers = input[0].split(",").map { it.toInt() }
    val boards = mutableListOf<Board>()

    input.subList(2, input.size).chunked(6).map { lines ->
        val columns = List(5) { mutableSetOf<Int>() }
        val rows = mutableListOf<MutableSet<Int>>()
        lines.subList(0, 5).forEach { line ->
            val lineNumbers = line.split(" ").filter { it.isNotBlank() }.map { it.toInt() }
            lineNumbers.forEachIndexed { index, n ->
                columns[index].add(n)
            }
            rows.add(lineNumbers.toMutableSet())
        }
        boards.add(Board(rows, columns))
    }

    numbers.forEach { n ->
        boards.forEach { board ->
            board.call(n)
            if (boards.all { it.hasWon() }) {
                return 1L * n * board.columns.sumOf { it.sum() }
            }
        }
    }

    return ""
}