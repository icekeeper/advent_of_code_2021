import java.io.File
import kotlin.math.max
import kotlin.math.min

@ExperimentalStdlibApi
fun main() {
    val input = File("src/main/resources/day22.txt").readLines()
    println(puzzleOne(input))
    println(puzzleTwo(input))
}

private fun puzzleOne(input: List<String>): Any {
    val onCubes = mutableSetOf<Triple<Int, Int, Int>>()
    input.forEach { line ->
        val (action, xr, yr, zr) = line.split(' ', ',')
        val (xMin, xMax) = xr.drop(2).split("..").map { it.toInt() }
        val (yMin, yMax) = yr.drop(2).split("..").map { it.toInt() }
        val (zMin, zMax) = zr.drop(2).split("..").map { it.toInt() }

        for (x in max(xMin, -50)..min(xMax, 50))
            for (y in max(yMin, -50)..min(yMax, 50))
                for (z in max(zMin, -50)..min(zMax, 50))
                    when (action) {
                        "on" -> onCubes.add(Triple(x, y, z))
                        "off" -> onCubes.remove(Triple(x, y, z))
                    }
    }
    return onCubes.size
}

@ExperimentalStdlibApi
private fun puzzleTwo(input: List<String>): Any {
    data class Point(val x: Int, val y: Int, val z: Int)

    data class Cuboid(val min: Point, val max: Point) {
        fun isIntersects(another: Cuboid) = another.min.x <= max.x && another.max.x >= min.x
                && another.min.y <= max.y && another.max.y >= min.y
                && another.min.z <= max.z && another.max.z >= min.z

        fun subtract(c: Cuboid): List<Cuboid> = buildList {
            if (c.max.z < max.z) add(Cuboid(Point(min.x, min.y, c.max.z + 1), max))
            if (c.min.z > min.z) add(Cuboid(min, Point(max.x, max.y, c.min.z - 1)))
            if (c.max.x < max.x) add(
                Cuboid(
                    Point(c.max.x + 1, min.y, max(min.z, c.min.z)),
                    Point(max.x, max.y, min(max.z, c.max.z))
                )
            )
            if (c.min.x > min.x) add(
                Cuboid(
                    Point(min.x, min.y, max(min.z, c.min.z)),
                    Point(c.min.x - 1, max.y, min(max.z, c.max.z))
                )
            )
            if (c.max.y < max.y) add(
                Cuboid(
                    Point(max(min.x, c.min.x), c.max.y + 1, max(min.z, c.min.z)),
                    Point(min(max.x, c.max.x), max.y, min(max.z, c.max.z))
                )
            )
            if (c.min.y > min.y) add(
                Cuboid(
                    Point(max(min.x, c.min.x), min.y, max(min.z, c.min.z)),
                    Point(min(max.x, c.max.x), c.min.y - 1, min(max.z, c.max.z))
                )
            )
        }

        fun size() = 1L * (max.x - min.x + 1) * (max.y - min.y + 1) * (max.z - min.z + 1)
    }

    var onCuboids = mutableListOf<Cuboid>()
    input.forEach { line ->
        val (action, xr, yr, zr) = line.split(' ', ',')
        val (xMin, xMax) = xr.drop(2).split("..").map { it.toInt() }
        val (yMin, yMax) = yr.drop(2).split("..").map { it.toInt() }
        val (zMin, zMax) = zr.drop(2).split("..").map { it.toInt() }

        val cuboid = Cuboid(Point(xMin, yMin, zMin), Point(xMax, yMax, zMax))
        onCuboids = onCuboids.flatMap {
            if (it.isIntersects(cuboid)) it.subtract(cuboid) else listOf(it)
        }.toMutableList()

        if (action == "on") {
            onCuboids.add(cuboid)
        }
    }
    return onCuboids.sumOf { it.size() }
}