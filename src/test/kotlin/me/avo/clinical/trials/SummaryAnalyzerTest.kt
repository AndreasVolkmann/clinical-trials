package me.avo.clinical.trials

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class SummaryAnalyzerTest {

    @Test
    fun testrun() {
        SummaryAnalyzer.run()
    }


    @Test
    fun analyzeLov() {
        val lov = "Quality of life"
        println("Analyze $lov")
        ClassLoader.loadKeywords()
                .filterValues { lov in it }
                .let { TrialCombiner.makeRaw(it) }
                .let { SummaryAnalyzer.findCleanedWords(it.map { it.summary }) }

    }

}