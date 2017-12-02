package me.avo.clinical.trials.komputation

fun main(args: Array<String>) = Args(args).run {
    if (help) return@run
    run(embeddingDimension, iterations, size)
}