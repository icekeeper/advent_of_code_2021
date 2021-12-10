import java.io.File

fun main() {
    val input = File("src/main/resources/day10.txt").readLines()
    println(puzzleOne(input))
    println(puzzleTwo(input))
}

private fun puzzleOne(input: List<String>): Any {
    return input.sumOf { line ->
        val stack = mutableListOf<Char>()
        line.forEach {
            when (it) {
                '(', '{', '<', '[' -> stack.add(it)
                ')' -> if (stack.last() == '(') stack.removeLast() else return@sumOf 3
                ']' -> if (stack.last() == '[') stack.removeLast() else return@sumOf 57
                '}' -> if (stack.last() == '{') stack.removeLast() else return@sumOf 1197
                '>' -> if (stack.last() == '<') stack.removeLast() else return@sumOf 25137
            }
        }
        return@sumOf 0L
    }
}

private fun puzzleTwo(input: List<String>): Any {
    val result = input.map { line ->
        val stack = mutableListOf<Char>()
        line.forEach {
            when (it) {
                '(', '{', '<', '[' -> stack.add(it)
                ')' -> if (stack.last() == '(') stack.removeLast() else return@map 0
                ']' -> if (stack.last() == '[') stack.removeLast() else return@map 0
                '}' -> if (stack.last() == '{') stack.removeLast() else return@map 0
                '>' -> if (stack.last() == '<') stack.removeLast() else return@map 0
            }
        }
        return@map stack.reversed().map {
            when (it) {
                '(' -> 1L
                '[' -> 2L
                '{' -> 3L
                '<' -> 4L
                else -> throw RuntimeException()
            }
        }.reduce { acc, i -> acc * 5 + i }
    }
        .filter { it > 0 }
        .sorted()
    return result[result.size / 2]
}