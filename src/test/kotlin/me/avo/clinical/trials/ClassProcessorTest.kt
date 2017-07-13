package me.avo.clinical.trials

import org.amshove.kluent.shouldBeLessOrEqualTo
import org.junit.jupiter.api.Test

internal class ClassProcessorTest {

    @Test
    fun summary() {
        val summaries = ClassLoader.loadSummaries()
        summaries.printTen()
    }

    @Test
    fun keywords() {
        val keys = ClassLoader.loadKeywords()
        //keys.filter { it.value.size > 1 }.values.sortedByDescending { it.size }.printTen()
        val processor = base(keys)
        val trials = processor.trimToAverage()
                .let {
                    it.values.forEach { it.size shouldBeLessOrEqualTo 4 }
                    TrialCombiner.make(it)
                }.sortedBy { it.summary.length }
                .filter { it.summary.isNotBlank() }

        trials.filter { it.summary.split(" ").size < 4 }
                .alsoPrint { "Trials with summary less than 2 words: ${it.size}" }
                .printTen()


        TrialCombiner.export(trials.take(20_000))
    }


    @Test
    fun common() {
        val keys = ClassLoader.loadKeywords()
        val trials = ClassProcessor(keys).let {
            val data = it.filterByCommon(5)
            val filtered = it.trimToAverage(data)
            TrialCombiner.make(filtered)
        }.filter { it.summary.contains(" ") }

        TrialCombiner.export(trials)
    }

    @Test
    fun conditions() {
        val conds = ClassLoader.loadConditions()
        base(conds)
    }

    @Test
    fun interventions() {
        val inter = ClassLoader.loadInterventions()
        base(inter)
    }

    fun base(data: Map<String, List<String>>) = ClassProcessor(data).apply {
        analyze()
        search("endoc")
    }

}