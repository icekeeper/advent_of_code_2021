import java.io.File

fun main() {
    val input = File("src/main/resources/day16.txt").readLines()
    println(puzzleOne(input))
    println(puzzleTwo(input))
}

private sealed class Packet(val version: Int, val typeId: Int)
private class Literal(version: Int, val value: Long) : Packet(version, 4)
private class Operator(version: Int, typeId: Int, val subPackets: List<Packet>) : Packet(version, typeId)

private fun Iterator<Char>.next(n: Int) = List(n) { next() }
private fun List<Char>.toInt() = this.joinToString("").toInt(2)

private fun parseLiteral(iterator: Iterator<Char>, version: Int): Literal {
    val n = StringBuilder()
    do {
        val chunk = iterator.next(5)
        chunk.drop(1).forEach(n::append)
    } while (chunk.first() == '1')
    return Literal(version, n.toString().toLong(2))
}

private fun parseSubPackets(iterator: Iterator<Char>, length: Int): List<Packet> {
    val subIterator = iterator.next(length).iterator()
    return generateSequence { if (subIterator.hasNext()) parsePacket(subIterator) else null }.toList()
}

private fun parseOperator(iterator: Iterator<Char>, version: Int, typeId: Int): Operator {
    return when (iterator.next()) {
        '0' -> Operator(version, typeId, parseSubPackets(iterator, iterator.next(15).toInt()))
        else -> Operator(version, typeId, List(iterator.next(11).toInt()) { parsePacket(iterator) })
    }
}

private fun parsePacket(iterator: Iterator<Char>): Packet {
    val version = iterator.next(3).toInt()
    return when (val typeId = iterator.next(3).toInt()) {
        4 -> parseLiteral(iterator, version)
        else -> parseOperator(iterator, version, typeId)
    }
}

private fun puzzleOne(input: List<String>): Any {
    val binary = input.first().asSequence().flatMap {
        it.digitToInt(16).toString(2).padStart(4, '0').asIterable()
    }
    val packet = parsePacket(binary.iterator())

    fun versionSum(p: Packet): Int =
        when (p) {
            is Literal -> p.version
            is Operator -> p.version + p.subPackets.sumOf(::versionSum)
        }

    return versionSum(packet)
}

private fun puzzleTwo(input: List<String>): Any {
    val binary = input.first().asSequence().flatMap {
        it.digitToInt(16).toString(2).padStart(4, '0').asIterable()
    }
    val packet = parsePacket(binary.iterator())

    fun evaluate(p: Packet): Long =
        when (p) {
            is Operator -> when (p.typeId) {
                0 -> p.subPackets.sumOf(::evaluate)
                1 -> p.subPackets.map(::evaluate).reduce(Long::times)
                2 -> p.subPackets.minOf(::evaluate)
                3 -> p.subPackets.maxOf(::evaluate)
                5 -> if (evaluate(p.subPackets.first()) > evaluate(p.subPackets.last())) 1 else 0
                6 -> if (evaluate(p.subPackets.first()) < evaluate(p.subPackets.last())) 1 else 0
                else -> if (evaluate(p.subPackets.first()) == evaluate(p.subPackets.last())) 1 else 0
            }
            is Literal -> p.value
        }

    return evaluate(packet)
}