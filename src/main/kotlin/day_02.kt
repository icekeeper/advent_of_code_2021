import java.io.File

fun main() {
    val input = File("src/main/resources/day02.txt").readLines()
    println(puzzleOne(input))
    println(puzzleTwo(input))
}

private fun puzzleOne(input: List<String>): Any {
    var x = 0
    var y = 0
    input.forEach { line ->
        val (command, n) = line.split(" ")
        when(command) {
            "forward" -> x += n.toInt()
            "down" -> y += n.toInt()
            "up" -> y -= n.toInt()
        }
    }
    return x * y
}

private fun puzzleTwo(input: List<String>): Any {
    var aim = 0L
    var x = 0L
    var y = 0L

    input.forEach { line ->
        val (command, n) = line.split(" ")
        when(command) {
            "forward" -> {
                x += n.toLong()
                y += n.toLong() * aim
            }
            "down" -> aim += n.toInt()
            "up" -> aim -= n.toInt()
        }
    }
    return x * y
}