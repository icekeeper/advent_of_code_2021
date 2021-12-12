import java.io.File

fun main() {
    val input = File("src/main/resources/day12.txt").readLines()
    println(puzzleOne(input))
    println(puzzleTwo(input))
}

private fun puzzleOne(input: List<String>): Any {
    val edges = input.map { it.split("-") }.map { (a, b) -> Pair(a, b) }
    val mem = mutableMapOf<Pair<String, Set<String>>, Long>()
    fun paths(to: String, forbidden: Set<String> = emptySet()): Long {
        if (to == "start") {
            return 1
        }
        if (!mem.containsKey(to to forbidden)) {
            mem[to to forbidden] = edges
                .filter { it.first == to || it.second == to }
                .filter { it.first !in forbidden && it.second !in forbidden }
                .sumOf {
                    val next = if (it.first == to) it.second else it.first
                    if (to.first().isUpperCase()) {
                        paths(next, forbidden)
                    } else {
                        paths(next, forbidden + to)
                    }
                }
        }
        return mem.getValue(to to forbidden)
    }

    return paths("end")
}

private fun puzzleTwo(input: List<String>): Any {
    val edges = input.map { it.split("-") }.map { (a, b) -> Pair(a, b) }
    var paths = 0L
    fun visit(v: String, visited: Set<String> = emptySet(), twice: Boolean = false) {
        if (v == "end") {
            paths++
        } else {
            edges
                .filter { it.first == v || it.second == v }
                .forEach {
                    val to = if (it.first == v) it.second else it.first
                    if (to != "start" && (to.first().isUpperCase() || to !in visited || !twice)) {
                        visit(to, visited + to, twice || to.first().isLowerCase() && to in visited)
                    }
                }
        }
    }

    visit("start")
    return paths
}