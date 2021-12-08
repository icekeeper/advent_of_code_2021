import java.io.File

fun main() {
    val input = File("src/main/resources/day03.txt").readLines()
    println(puzzleOne(input))
    println(puzzleTwo(input))
}

private fun puzzleOne(input: List<String>): Any {
    val freq = input.map { it.map { c -> if (c == '1') 1 else 0 } }
        .reduce { a, b -> a.zip(b).map { (c1, c2) -> c1 + c2 } }
    val gamma = freq.mapIndexed { index, i -> if (i > input.size / 2) (1L shl (freq.size - 1 - index)) else 0L }.sum()
    val epsilon = freq.mapIndexed { index, i -> if (i < input.size / 2) (1L shl (freq.size - 1 - index)) else 0L }.sum()
    return gamma * epsilon
}

private fun puzzleTwo(input: List<String>): Any {
    var oxygen = input.map { it.map { c -> if (c == '1') 1 else 0 } }
    var bit = 0
    while (oxygen.size > 1) {
        val freq = oxygen.reduce { a, b -> a.zip(b).map { (c1, c2) -> c1 + c2 } }
        oxygen = oxygen.filter { it[bit] == if (freq[bit] >= oxygen.size / 2) 1 else 0 }
        bit++
    }
    val o = oxygen.first().mapIndexed { index, i -> i * (1L shl (oxygen.first().size - 1 - index)) }.sum()

    var co2 = input.map { it.map { c -> if (c == '1') 1 else 0 } }
    bit = 0
    while (co2.size > 1) {
        val freq = co2.reduce { a, b -> a.zip(b).map { (c1, c2) -> c1 + c2 } }
        co2 = co2.filter { it[bit] == if (freq[bit] >= co2.size / 2) 0 else 1 }
        bit++
    }
    val c = co2.first().mapIndexed { index, i -> i * (1L shl (co2.first().size - 1 - index)) }.sum()

    return o * c
}

