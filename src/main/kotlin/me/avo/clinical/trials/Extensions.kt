package me.avo.clinical.trials

fun <T> List<T>.printTen() = take(10).onEach { println(it) }

fun <R, T> Map<R, T>.printTen() = toList().printTen()

fun <T> Collection<T>.print(i: Int = size) = also { take(i).onEach { println(it) } }
fun <R, T> Map<R, T>.print(i: Int = size) = also { entries.take(i).onEach { println(it) } }

fun <T> T.alsoPrint(block: (T) -> String): T = apply {
    println(block(this))
}


fun toPercent(part: Int, total: Int) = part.toDouble() / total.toDouble() * 100


fun formatPercent(part: Int, total: Int) = toPercent(part, total).let {
    "$part / $total ( ${
    it.toString().let {
        it.substringBefore(".") + "." + it.substringAfter(".").take(2)
    }}% )"
}