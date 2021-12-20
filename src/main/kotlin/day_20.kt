import java.io.File

fun main() {
    val input = File("src/main/resources/day20.txt").readLines()
    println(puzzleOne(input))
    println(puzzleTwo(input))
}

private class Image(val pixels: List<List<Int>>, val infiniteColor: Int) {
    fun enhance(enhancement: List<Int>): Image {
        fun enhancedPixel(i: Int, j: Int): Int {
            val s = StringBuilder()
            for (i1 in i - 1..i + 1) {
                for (j1 in j - 1..j + 1) {
                    s.append(if (i1 in pixels.indices && j1 in pixels[i1].indices) pixels[i1][j1] else infiniteColor)
                }
            }
            return enhancement[s.toString().toInt(2)]
        }

        val newPixels = List(pixels.size + 2) { i ->
            List(pixels.first().size + 2) { j ->
                enhancedPixel(i - 1, j - 1)
            }
        }
        val newColor = if (infiniteColor == 0) enhancement.first() else enhancement.last()
        return Image(newPixels, newColor)
    }
}

private fun puzzleOne(input: List<String>): Any {
    val enhancement = input.first().map { if (it == '#') 1 else 0 }
    val image = Image(input.drop(2).map { line -> line.map { if (it == '#') 1 else 0 } }, 0)

    return image.enhance(enhancement).enhance(enhancement).pixels.sumOf { it.count { c -> c == 1 } }
}

private fun puzzleTwo(input: List<String>): Any {
    val enhancement = input.first().map { if (it == '#') 1 else 0 }
    val image = Image(input.drop(2).map { line -> line.map { if (it == '#') 1 else 0 } }, 0)

    return generateSequence(image) { it.enhance(enhancement) }.elementAt(50)
        .pixels.sumOf { it.count { c -> c == 1 } }
}