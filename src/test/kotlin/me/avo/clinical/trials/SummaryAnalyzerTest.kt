package me.avo.clinical.trials

import me.avo.clinical.trials.processing.*
import org.junit.jupiter.api.Test

internal class SummaryAnalyzerTest {

    @Test
    fun testrun() {
        SummaryAnalyzer.run()
    }


    @Test
    fun analyzeLov() {
        val lov = "Exercise"
        println("Analyze: $lov")
        ClassLoader.loadKeywords()
                .filterValues { lov in it }
                .let(TrialCombiner::makeRaw)
                .let { SummaryAnalyzer.findCleanedWords(it.map { it.summary }, 30) }

    }

}