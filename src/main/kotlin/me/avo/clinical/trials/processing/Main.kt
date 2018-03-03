package me.avo.clinical.trials.processing

import me.avo.clinical.trials.processing.db.*
import java.io.*

fun main(args: Array<String>) {
    DatabaseService().fetchTrialsFromDb(5)
}



//val data = ClassLoader.loadKeywords()
//val keywords = ClassProcessor(data).trimToAverage()
//val trials = TrialCombiner.make(keywords)
//Classifier().spark(trials)