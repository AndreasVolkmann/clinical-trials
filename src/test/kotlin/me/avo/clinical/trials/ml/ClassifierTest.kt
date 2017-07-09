package me.avo.clinical.trials.ml

import me.avo.clinical.trials.ClassLoader
import me.avo.clinical.trials.ClassProcessor
import me.avo.clinical.trials.TrialCombiner
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class ClassifierTest {


    @Test
    fun encoder() = Classifier(listOf()).encoder


    @Test
    fun spark() {
        val data = ClassLoader.loadKeywords()
        val keywords = ClassProcessor(data).let {
            it.trimToAverage()
        }
        val trials = TrialCombiner.make(keywords)

        Classifier(trials).spark()

    }

}