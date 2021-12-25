import java.io.File

fun main() {
    val input = File("src/main/resources/day24.txt").readLines()
    puzzleOne(input)
    println(puzzleTwo(input))
}

private data class Variable(var value: Long) {
    override fun toString() = value.toString()
}

private class Monad(val instructions: List<Instruction>) {
    val x = Variable(0)
    val y = Variable(0)
    val z = Variable(0)
    val w = Variable(0)

    var input: String = ""
    var digit: Int = 0

    fun reset() {
        x.value = 0
        y.value = 0
        z.value = 0
        w.value = 0
    }

    operator fun get(c: String) = when (c) {
        "x" -> x
        "y" -> y
        "z" -> z
        "w" -> w
        else -> Variable(c.toLong())
    }

    fun peekNextDigit(): String {
        return if (digit < input.length) input[digit].toString() else "NONE"
    }

    fun nextDigit(): Long {
        return input[digit++].digitToInt().toLong()
    }

    fun run(input: String) {
        reset()
        this.input = input
        instructions.forEach { it.invoke(this) }
    }

    fun trace(input: String) {
        reset()
        this.input = input
        instructions.forEachIndexed { index, instruction ->
            println("----------")
            println("x=$x y=$y z=$z w=$w next=${peekNextDigit()} digit=$digit")
            println("${index + 1}: $instruction")
            instruction.invoke(this)
            println("----------")
        }
        println("x=$x y=$y z=$z w=$w")
    }
}

private sealed class Instruction {
    abstract fun invoke(m: Monad)
}

private class Inp(val a: String) : Instruction() {
    override fun invoke(m: Monad) {
        m[a].value = m.nextDigit()
    }

    override fun toString(): String = "inp $a"
}

private class Set(val a: String, val b: String) : Instruction() {
    override fun invoke(m: Monad) {
        m[a].value = m[b].value
    }

    override fun toString(): String = "$a = $b"
}

private class Add(val a: String, val b: String) : Instruction() {
    override fun invoke(m: Monad) {
        m[a].value += m[b].value
    }

    override fun toString(): String = "$a = $a + $b"
}

private class Mul(val a: String, val b: String) : Instruction() {
    override fun invoke(m: Monad) {
        m[a].value *= m[b].value
    }

    override fun toString(): String = "$a = $a * $b"
}

private class Div(val a: String, val b: String) : Instruction() {
    override fun invoke(m: Monad) {
        m[a].value /= m[b].value
    }

    override fun toString(): String = "$a = $a / $b"
}

private class Mod(val a: String, val b: String) : Instruction() {
    override fun invoke(m: Monad) {
        m[a].value %= m[b].value
    }

    override fun toString(): String = "$a = $a % $b"
}

private class Eql(val a: String, val b: String) : Instruction() {
    override fun invoke(m: Monad) {
        m[a].value = if (m[a].value == m[b].value) 1 else 0
    }

    override fun toString(): String = "$a = $a == $b"
}

private fun puzzleOne(input: List<String>) {
    val instructions = input.map { line ->
        val tokens = line.split(" ")
        when (tokens[0]) {
            "inp" -> Inp(tokens[1])
            "add" -> Add(tokens[1], tokens[2])
            "mul" -> Mul(tokens[1], tokens[2])
            "div" -> Div(tokens[1], tokens[2])
            "mod" -> Mod(tokens[1], tokens[2])
            "eql" -> Eql(tokens[1], tokens[2])
            "set" -> Set(tokens[1], tokens[2])
            else -> throw RuntimeException("unknown command $line")
        }
    }

    val monad = Monad(instructions)
    monad.trace("51121176121391")
}

private fun puzzleTwo(input: List<String>): Any {
    //solved manually after deducing MONAD algorithm
    return ""
}