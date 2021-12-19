import java.io.File
import kotlin.math.abs
import kotlin.math.max

fun main() {
    val input = File("src/main/resources/day19.txt").readLines()
    println(puzzleOne(input))
    println(puzzleTwo(input))
}

private data class Point(val x: Int, val y: Int, val z: Int) {
    infix fun distanceTo(p: Point) = abs(x - p.x) + abs(y - p.y) + abs(z - p.z)
}

private data class Scan(val points: List<Point>, val position: Point = Point(0, 0, 0)) {
    fun rotateX(): Scan = Scan(points.map { Point(it.x, -it.z, it.y) })
    fun rotateY(): Scan = Scan(points.map { Point(-it.z, it.y, it.x) })
    fun rotateCounterY(): Scan = Scan(points.map { Point(it.z, it.y, -it.x) })
    fun rotateZ(): Scan = Scan(points.map { Point(-it.y, it.x, it.z) })

    fun rotationsX() = generateSequence(this) { it.rotateX() }.take(4)
    fun rotationsZ() = generateSequence(this) { it.rotateZ() }.take(4)

    fun permutations() = sequence {
        rotationsZ().flatMap { it.rotationsX() }.forEach { yield(it) }
        rotateY().rotationsX().forEach { yield(it) }
        rotateCounterY().rotationsX().forEach { yield(it) }
    }

    fun shift(dx: Int, dy: Int, dz: Int) =
        Scan(points.map { Point(it.x + dx, it.y + dy, it.z + dz) }, Point(dx, dy, dz))

    fun intersects(s: Scan) = points.intersect(s.points.toSet()).size

    fun alignBy(s: Scan): Scan? {
        return permutations().firstNotNullOfOrNull { p ->
            p.points.firstNotNullOfOrNull { pp ->
                s.points.asSequence()
                    .map { sp -> p.shift(sp.x - pp.x, sp.y - pp.y, sp.z - pp.z) }
                    .find { s.intersects(it) >= 12 }
            }
        }
    }
}

private fun parse(input: List<String>): List<Scan> {
    val result = mutableListOf<Scan>()
    var points = mutableListOf<Point>()
    input.forEach { line ->
        when {
            line.startsWith("---") -> points = mutableListOf()
            line.isBlank() -> result.add(Scan(points))
            else -> {
                val (x, y, z) = line.split(',').map(String::toInt)
                points.add(Point(x, y, z))
            }
        }
    }
    result.add(Scan(points))
    return result
}

private fun puzzleOne(input: List<String>): Any {
    val scans = parse(input)
    val aligned = mutableListOf(scans.first())
    val notAligned = (1..scans.indices.last).toMutableSet()
    while (notAligned.isNotEmpty()) {
        for (i in scans.indices) {
            if (i in notAligned) {
                aligned.firstNotNullOfOrNull { scans[i].alignBy(it) }?.let {
                    aligned.add(it)
                    notAligned.remove(i)
                }
            }
        }
    }
    return aligned.flatMap { it.points }.toSet().size
}

private fun puzzleTwo(input: List<String>): Any {
    val scans = parse(input)
    val aligned = mutableListOf(scans.first())
    val notAligned = (1..scans.indices.last).toMutableSet()
    while (notAligned.isNotEmpty()) {
        for (i in scans.indices) {
            if (i in notAligned) {
                aligned.firstNotNullOfOrNull { scans[i].alignBy(it) }?.let {
                    aligned.add(it)
                    notAligned.remove(i)
                }
            }
        }
    }
    var max = 0
    for (i in aligned.indices) {
        for (j in aligned.indices) {
            max = max(aligned[i].position distanceTo aligned[j].position, max)
        }
    }
    return max
}