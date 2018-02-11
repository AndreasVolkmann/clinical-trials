package me.avo.clinical.trials.processing

import java.io.*

object ClassLoader {

    private const val date = ""
    private val system: String = System.getProperty("os.name")
    val dir = if (system.contains("Windows", true)) "D:\\DL\\clinical-trials$date\\"
    else "/Users/av/Desktop/dev/clinical-trials-20170618/"

    const val delimiter = "|"

    const val summaryFile = "brief_summaries.txt"
    const val detailSummaryFile = "detailed_descriptions.txt"
    const val keywordFile = "keywords.txt"

    fun <T : Any> extract(name: String, body: (split: List<String>) -> T): List<T> = File(dir + name)
        .useLines { processSequence(it, body) }

    fun <T : Any> processSequence(seq: Sequence<String>, body: (List<String>) -> T) = seq
        .drop(1)
        .filter(String::isNotBlank)
        .map { it.split(delimiter) }
        .map(body)
        .toList()

    fun loadSummaries() = extract(summaryFile) {
        it[1] to it[2].trimQuotesAndSpace()
    }.toMap()

    fun loadDetailedSummaries() = extract(detailSummaryFile) {
        it[1] to it[2].trimQuotesAndSpace()
    }.toMap()

    fun loadKeywords() = loadPair(keywordFile)
        .mapValues { it.value.filterNot { it in lovsToIgnore } }
        .filter { it.value.isNotEmpty() }

    fun loadConditions() = loadPair("browse_conditions.txt")

    fun loadInterventions() = loadPair("browse_interventions.txt")

    fun loadPair(fileName: String) = extract(fileName)
    { it[1] to it[2].split(" ").joinToString(" ", transform = String::capitalize) }
        .groupBy { it.first }
        .mapValues { it.value.map { it.second } }

}