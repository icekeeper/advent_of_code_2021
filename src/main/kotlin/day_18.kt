import java.io.File
import kotlin.math.max

fun main() {
    val input = File("src/main/resources/day18.txt").readLines()
    println(puzzleOne(input))
    println(puzzleTwo(input))
}

private sealed class Number(var parent: Pair?) {
    operator fun plus(n: Number): Number = Pair(this.copy(), n.copy()).reduce()

    fun isRight() = parent?.let { it.right == this } ?: false
    fun isLeft() = parent?.let { it.left == this } ?: false
    fun level(): Int = parent?.let { it.level() + 1 } ?: 0

    abstract fun rightmost(): RegularNumber
    abstract fun leftmost(): RegularNumber
    abstract fun magnitude(): Long
    abstract fun copy(): Number

    fun traverse(): Sequence<Number> = sequence {
        when (this@Number) {
            is RegularNumber -> yield(this@Number)
            is Pair -> {
                yieldAll(this@Number.left.traverse())
                yield(this@Number)
                yieldAll(this@Number.right.traverse())
            }
        }
    }

    fun reduce(): Number {
        var reduced: Boolean
        do {
            reduced = false
            traverse().filterIsInstance<Pair>().find { it.level() == 4 }?.let {
                reduced = true
                it.explode()
            }
            if (!reduced) {
                traverse().filterIsInstance<RegularNumber>().find { it.value >= 10 }?.let {
                    reduced = true
                    it.split()
                }
            }
        } while (reduced)
        return this
    }
}

private class RegularNumber(var value: Int, parent: Pair? = null) : Number(parent) {
    fun split() {
        val first = RegularNumber(value / 2)
        val second = RegularNumber(value / 2 + value % 2)
        val pair = Pair(first, second, parent)
        if (isLeft()) parent?.left = pair else parent?.right = pair
    }

    override fun rightmost(): RegularNumber = this
    override fun leftmost(): RegularNumber = this
    override fun copy(): Number = RegularNumber(value)

    fun leftNeighbour(): RegularNumber? {
        var p: Number? = this
        while (p != null && p.isLeft()) p = p.parent
        return p?.parent?.left?.rightmost()
    }

    fun rightNeighbour(): RegularNumber? {
        var p: Number? = this
        while (p != null && p.isRight()) p = p.parent
        return p?.parent?.right?.leftmost()
    }

    override fun magnitude(): Long = value.toLong()

    override fun toString(): String = value.toString()
}

private class Pair(var left: Number, var right: Number, parent: Pair? = null) : Number(parent) {
    init {
        left.parent = this
        right.parent = this
    }

    fun explode() {
        (left as RegularNumber).leftNeighbour()?.let { it.value += (left as RegularNumber).value }
        (right as RegularNumber).rightNeighbour()?.let { it.value += (right as RegularNumber).value }
        val number = RegularNumber(0, parent)
        if (isLeft()) parent?.left = number else parent?.right = number
    }

    override fun magnitude(): Long = 3 * left.magnitude() + 2 * right.magnitude()

    override fun rightmost(): RegularNumber = right.rightmost()
    override fun leftmost(): RegularNumber = left.leftmost()
    override fun copy(): Number = Pair(left.copy(), right.copy())

    override fun toString(): String = "[$left,$right]"
}

private fun parse(i: Iterator<Char>): Number {
    val t = i.next()
    if (t.isDigit()) return RegularNumber(t.digitToInt())
    val first = parse(i)
    i.next()
    val second = parse(i)
    i.next()
    return Pair(first, second)
}

private fun puzzleOne(input: List<String>): Any {
    val numbers = input.map { parse(it.iterator()) }
    return numbers.reduce(Number::plus).magnitude()
}

private fun puzzleTwo(input: List<String>): Any {
    val numbers = input.map { parse(it.iterator()) }
    var max = 0L
    for (i in numbers.indices) {
        for (j in numbers.indices) {
            if (i != j) {
                max = max(max, (numbers[i] + numbers[j]).magnitude())
            }
        }
    }
    return max
}