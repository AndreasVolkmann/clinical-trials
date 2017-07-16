package me.avo.clinical.trials

import java.io.File

object ClassLoader {

    val system: String = System.getProperty("os.name")
    val dir = if (system.contains("Windows", true)) "D:\\DL\\clinical-trials\\"
    else "/Users/av/Desktop/dev/clinical-trials-20170618/"

    const val delimiter = "|"

    const val summaryFile = "brief_summaries.txt"
    const val keywordFile = "keywords.txt"

    fun <T : Any> extract(name: String, body: (split: List<String>) -> T): List<T> = File(dir + name).useLines { lines ->
        lines.drop(1).filter { it.isNotBlank() }.map {
            val split = it.split(delimiter)
            body(split)
        }.toList()
    }

    fun loadSummaries() = extract(summaryFile) {
        it[1] to it[2].removeSurrounding("\"").trim()
    }.toMap()

    fun loadKeywords() = loadPair(keywordFile)

    fun loadConditions() = loadPair("browse_conditions.txt")

    fun loadInterventions() = loadPair("browse_interventions.txt")


    fun loadPair(fileName: String) = extract(fileName)
    { it[1] to it[2].split(" ").map { it.capitalize() }.joinToString(" ") }
            .groupBy { it.first }
            .mapValues { it.value.map { it.second } }

}