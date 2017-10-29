package me.avo.clinical.trials

import org.amshove.kluent.shouldBeLessOrEqualTo
import org.junit.jupiter.api.Test

internal class ClassProcessorTest {

    @Test
    fun summary() {
        val summaries = ClassLoader.loadSummaries()
        summaries.printTen()
    }

    @Test fun keywords() {
        val keys = ClassLoader.loadKeywords()
        val processor = base(keys)
<<<<<<< HEAD
        val trials = processor.trimToAverage()
                .let {
                    it.values.forEach { it.size shouldBeLessOrEqualTo 4 }
                    TrialCombiner.make(it)
                }.sortedBy { it.summary.length }

        val numWords = 10
        trials.filter { it.summary.split(" ").size < numWords }
                .alsoPrint { "Trials with summary less than $numWords words: ${it.size}" }
                .printTen()


        TrialCombiner.export(trials.take(30_000))
=======
        processor.trimToAverage()
                .let { TrialCombiner.make(it) } // make Trials
                .filter { it.summary.split(" ").size > 6 } // at least 1 space / 2 words
                .groupBy { it.keywords.first() } // group by 1st LOV
                .flatMap { it.value.take(processor.sizeThreshold) } // take 50 from each
                .let { TrialCombiner.export(it) }
    }


    @Test
    fun findTrialsWithLov() {
        val lov = "Cancer"
        ClassLoader.loadKeywords()
                .filter { it.value.contains(lov) }
                .let { ClassProcessor(it).trimToAverage() }
                .let { keys -> TrialCombiner.make(keys) }
                .print(20)
>>>>>>> bcc566020a40c5230c91aa02b7dc6e9aac9481a0
    }


    @Test
    fun common() {
        val keys = ClassLoader.loadKeywords()
        val trials = ClassProcessor(keys).let {
            val data = it.filterByCommon(30)
            val filtered = it.trimToAverage(data)
            TrialCombiner.make(filtered)
        }.filter { it.summary.contains(" ") }
        //.also { it.distinctBy { it.keywords.first() }.onEach { println(it) } }

        TrialCombiner.export(trials)
    }

    @Test
    fun conditions() {
        val conds = ClassLoader.loadConditions().print(10)
        base(conds)
    }

    @Test
    fun interventions() {
        val inter = ClassLoader.loadInterventions()
        base(inter)
    }

    fun base(data: Map<String, List<String>>) = ClassProcessor(data).apply {
        analyze()
        //search("endoc")
    }

    @Test
    fun `find LOVs that appear in more than one list`() {
        val keys = ClassLoader.loadKeywords().flatMap { it.value }.distinct()
        val conds = ClassLoader.loadConditions().flatMap { it.value }.distinct()
        val inter = ClassLoader.loadInterventions().flatMap { it.value }.distinct()
        (keys + conds + inter).groupBy { it }.filter { it.value.size > 1 }.forEach { key, list ->
            println("$key, ${list.size}")
        }

    }

}