import java.io.File
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

fun main() {
    val input = File("src/main/resources/day23.txt").readLines()
    println(puzzleOne(input))
    println(puzzleTwo(input))
}

private fun puzzleOne(input: List<String>): Any {
    val target = mapOf('A' to 3, 'B' to 5, 'C' to 7, 'D' to 9)

    val stepEnergy = mapOf('A' to 1, 'B' to 10, 'C' to 100, 'D' to 1000)
    val allowedX = listOf(1, 2, 4, 6, 8, 10, 11)

    val field = input.map { it.toMutableList() }

    fun targetX(c: Char) = target.getValue(c)

    fun moveCost(fy: Int, fx: Int, ty: Int, tx: Int) =
        (abs(fy - ty) + abs(fx - tx)) * stepEnergy.getValue(field[fy][fx])

    fun isHome(y: Int, x: Int) =
        (x == targetX(field[y][x])) && (y == 3 || (y == 2 && field[3][targetX(field[y][x])] == field[y][x]))

    fun isAllInPlace() = target.all { (c, x) -> field[2][x] == c && field[3][x] == c }

    fun hallClear(fromX: Int, toX: Int) =
        allowedX.filter { it > min(fromX, toX) && it < max(fromX, toX) }.all { field[1][it] == '.' }

    fun moveOutAllowed(fy: Int, fx: Int, tx: Int) = field[fy][fx] != '.'
            && !isHome(fy, fx)
            && (fy == 2 || field[2][fx] == '.')
            && hallClear(fx, tx)

    fun moveInAllowed(fromX: Int) = field[1][fromX] != '.'
            && field[2][targetX(field[1][fromX])] == '.'
            && (field[3][targetX(field[1][fromX])] == '.' || field[3][targetX(field[1][fromX])] == field[1][fromX])
            && hallClear(fromX, targetX(field[1][fromX]))

    fun search(): Int {
        fun step(fy: Int, fx: Int, ty: Int, tx: Int): Int {
            val moveCost = moveCost(fy, fx, ty, tx)
            field[ty][tx] = field[fy][fx]
            field[fy][fx] = '.'
            val cost = search()
            val result = if (cost == Integer.MAX_VALUE) Integer.MAX_VALUE else moveCost + cost
            field[fy][fx] = field[ty][tx]
            field[ty][tx] = '.'
            return result
        }

        if (isAllInPlace()) {
            return 0
        }

        val fxIn = allowedX.find(::moveInAllowed)
        return if (fxIn != null) {
            val tx = targetX(field[1][fxIn])
            val ty = if (field[3][tx] == '.') 3 else 2
            step(1, fxIn, ty, tx)
        } else {
            var min = Integer.MAX_VALUE
            for ((_, fx) in target) {
                for (tx in allowedX) {
                    if (field[1][tx] == '.') {
                        if (moveOutAllowed(2, fx, tx)) {
                            min = min(min, step(2, fx, 1, tx))
                        }

                        if (moveOutAllowed(3, fx, tx)) {
                            min = min(min, step(3, fx, 1, tx))
                        }
                    }
                }
            }
            min
        }
    }

    return search()
}

private fun puzzleTwo(input: List<String>): Any {
    val target = mapOf('A' to 3, 'B' to 5, 'C' to 7, 'D' to 9)

    val stepEnergy = mapOf('A' to 1, 'B' to 10, 'C' to 100, 'D' to 1000)
    val allowedX = listOf(1, 2, 4, 6, 8, 10, 11)

    val field = input.map { it.toMutableList() }

    fun targetX(c: Char) = target[c] ?: Integer.MAX_VALUE

    fun moveCost(fy: Int, fx: Int, ty: Int, tx: Int) =
        (abs(fy - ty) + abs(fx - tx)) * stepEnergy.getValue(field[fy][fx])

    fun isHome(y: Int, x: Int) = (y..5).all { targetX(field[it][x]) == x }
    fun isAllInPlace() = target.all { (c, x) -> (2..5).all { field[it][x] == c } }

    fun hallClear(fromX: Int, toX: Int) =
        allowedX.filter { it > min(fromX, toX) && it < max(fromX, toX) }.all { field[1][it] == '.' }

    fun moveOutAllowed(fy: Int, fx: Int, tx: Int) = field[fy][fx] != '.'
            && !isHome(fy, fx)
            && ((2 until fy).all { field[it][fx] == '.' })
            && hallClear(fx, tx)

    fun moveInAllowed(fromX: Int) = field[1][fromX] != '.'
            && (2..5).all { field[it][targetX(field[1][fromX])] in setOf('.', field[1][fromX]) }
            && hallClear(fromX, targetX(field[1][fromX]))

    fun search(): Int {
        fun step(fy: Int, fx: Int, ty: Int, tx: Int): Int {
            val moveCost = moveCost(fy, fx, ty, tx)
            field[ty][tx] = field[fy][fx]
            field[fy][fx] = '.'
            val cost = search()
            val result = if (cost == Integer.MAX_VALUE) Integer.MAX_VALUE else moveCost + cost
            field[fy][fx] = field[ty][tx]
            field[ty][tx] = '.'
            return result
        }

        if (isAllInPlace()) {
            return 0
        }

        val fxIn = allowedX.find(::moveInAllowed)
        return if (fxIn != null) {
            val tx = targetX(field[1][fxIn])
            val ty = (5 downTo 2).find { field[it][tx] == '.' }!!
            step(1, fxIn, ty, tx)
        } else {
            var min = Integer.MAX_VALUE
            for ((_, fx) in target) {
                for (tx in allowedX) {
                    if (field[1][tx] == '.') {
                        for (fy in 2..5) {
                            if (moveOutAllowed(fy, fx, tx)) {
                                min = min(min, step(fy, fx, 1, tx))
                            }
                        }
                    }
                }
            }
            min
        }
    }

    return search()
}