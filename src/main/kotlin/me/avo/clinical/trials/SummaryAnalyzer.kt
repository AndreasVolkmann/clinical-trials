package me.avo.clinical.trials

object SummaryAnalyzer {

    fun run() {
        val trials = TrialCombiner.summaries.values.toList()
        findCommonWords(trials.take(100_000))
    }

    fun findCommonWords(trials: List<String>) = trials
            .map { TrialCombiner.cleanText(it) }
            .alsoPrint { "Cleaned ${it.size} trials" }
            .flatMap { it.split(" ") }
            .alsoPrint { "Split into ${it.size} words" }
            .filterWords()
            .analyze()


    fun List<String>.analyze() = also {
        it.groupBy { it }
                .map { it.key to it.value.size }
                .sortedByDescending { it.second }
                .filterNot { it.first in ignore }
                .printTen()
                .sumBy { it.second }
                .alsoPrint { "Total words: $it (for above LOVs)" }
    }


    /**
     * Find words that would be cleaned and show their relative occurrence
     */
    fun findCleanedWords(trials: List<String>, i: Int = 10) = trials
            .map { TrialCombiner.cleanText(it) }
            .map { it.split(" ").filter { it in words } }
            .flatMap { it.distinct() }
            .groupBy { it }
            .map { it.key to it.value.size }
            .sortedByDescending { it.second }
            .take(i)
            .forEach { (lov, size) -> println("$lov: ${formatPercent(size, trials.size)}") }





}