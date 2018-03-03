package me.avo.clinical.trials.processing

fun <T> List<T>.printTen() = take(10).onEach(::println)

fun <R, T> Map<R, T>.printTen(): List<Pair<R, T>> = toList().printTen()

fun <R : Collection<*>> R.print(i: Int = size): R = also { take(i).onEach(::println) }

fun <R, T> Map<R, T>.print(i: Int = size): Map<R, T> = also { entries.take(i).onEach(::println) }

fun <T> T.alsoPrint(block: (T) -> String): T = apply { println(block(this)) }

fun toPercent(part: Int, total: Int) = part.toDouble() / total.toDouble() * 100

fun formatPercent(part: Int, total: Int) = toPercent(part, total).let {
    "$part / $total ( ${
    it.toString().let {
        it.substringBefore(".") + "." + it.substringAfter(".").take(2)
    }}% )"
}

fun String.trimQuotesAndSpace() = removeSurrounding("\"").trim()
fun String.removeNewlineTrimSpaces() = replace("\n", "").replace("( +)"," ")