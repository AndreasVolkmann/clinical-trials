package me.avo.clinical.trials

import java.io.File

object TrialCombiner {

    val del = "|"
    val summaries = ClassLoader.loadSummaries()

    fun make(data: Map<String, List<String>>): List<Trial> = data
            .filter { summaries.containsKey(it.key) }
            .map { (id, keywords) ->
                Trial(id, keywords, summaries [id]!!)
            }.alsoPrint { "Was able to combine ${it.size} Trials" }


    fun export(trials: List<Trial>) = File("trials.txt").printWriter().use { out ->
        out.println("Label${del}Summary")
        trials.map { listOf(it.keywords.first().clean(), it.summary.clean()) }
                .map { it.map { it.clean() } }
                .filter { it.all { it.isNotBlank() } }
                .map { it.joinToString(del) }
                .alsoPrint { "Exporting ${it.size} Trials" }
                .forEach { out.println(it) }
    }

    fun String.clean() = this
            .replace(Regex("[^a-zA-Z]+"), " ")
            .replace(del, " ")
            .replace("\"", "")
            .replace(Regex("\\s+"), " ")
            .trim()

}