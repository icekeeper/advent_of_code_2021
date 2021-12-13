import java.io.File

fun main() {
    val input = File("src/main/resources/day13.txt").readLines()
    puzzleOne(input)
    puzzleTwo(input)
}

private fun puzzleOne(input: List<String>) {
    val points = input.takeWhile { it.isNotBlank() }
        .map { it.split(",") }
        .map { (x, y) -> x.toInt() to y.toInt() }.toSet()
    val folds = input.takeLastWhile { it.isNotBlank() }
        .map { it.removePrefix("fold along ").split("=") }
        .map { (axis, value) -> axis to value.toInt() }

    fun foldY(yf: Int, p: Set<Pair<Int, Int>>) =
        p.map { (x, y) -> if (y < yf) x to y else x to (2 * yf - y) }.toSet()

    fun foldX(xf: Int, p: Set<Pair<Int, Int>>) =
        p.map { (x, y) -> if (x < xf) x to y else (2 * xf - x) to y }.toSet()

    val fold = folds.first()
    when (fold.first) {
        "x" -> println(foldX(fold.second, points).size)
        "y" -> println(foldY(fold.second, points).size)
    }
}

private fun puzzleTwo(input: List<String>) {
    val points = input.takeWhile { it.isNotBlank() }
        .map { it.split(",") }
        .map { (x, y) -> x.toInt() to y.toInt() }.toSet()
    val folds = input.takeLastWhile { it.isNotBlank() }
        .map { it.removePrefix("fold along ").split("=") }
        .map { (axis, value) -> axis to value.toInt() }

    fun foldY(yf: Int, p: Set<Pair<Int, Int>>) =
        p.map { (x, y) -> if (y < yf) x to y else x to (2 * yf - y) }.toSet()

    fun foldX(xf: Int, p: Set<Pair<Int, Int>>) =
        p.map { (x, y) -> if (x < xf) x to y else (2 * xf - x) to y }.toSet()

    val result = folds.fold(points) { acc, fold ->
        when (fold.first) {
            "x" -> foldX(fold.second, acc)
            "y" -> foldY(fold.second, acc)
            else -> throw RuntimeException()
        }
    }

    val maxX = result.maxByOrNull { it.first }!!.first
    val maxY = result.maxByOrNull { it.second }!!.second

    for (y in 0..maxY) {
        for (x in 0..maxX) {
            if (x to y in result) {
                print("#")
            } else {
                print(".")
            }
        }
        println()
    }
}