package me.avo.clinical.trials.processing

fun main(args: Array<String>) {
    val data = ClassLoader.loadKeywords()
    val keywords = ClassProcessor(data).trimToAverage()
    val trials = TrialCombiner.make(keywords)
    //Classifier().spark(trials)
}