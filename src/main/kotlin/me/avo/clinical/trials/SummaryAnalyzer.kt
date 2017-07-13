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


}