package me.avo.clinical.trials

import me.avo.clinical.trials.ml.Classifier
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
        val trials = processor.trimToAverage().let {
            it.values.forEach { it.size shouldBeLessOrEqualTo 4 }
            TrialCombiner.make(it)
        }

        //Classifier(trials).run()
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