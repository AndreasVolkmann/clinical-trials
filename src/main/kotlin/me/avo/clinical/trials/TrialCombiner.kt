package me.avo.clinical.trials

import java.io.File

object TrialCombiner {

    const val del = "|"
    val summaries = ClassLoader.loadSummaries()

    fun make(data: Map<String, List<String>>): List<Trial> = data
            .filter { summaries.containsKey(it.key) }
            .map { (id, keywords) -> Trial(id, keywords, summaries[id]!!) }
            .alsoPrint { "Was able to combine ${it.size} Trials" }


    fun export(trials: List<Trial>) = File("trials.txt").printWriter().use { out ->
        val headers = listOf("Label", "Summary")
        out.println(headers.joinToString(del))
        trials.map { listOf(it.keywords.first().clean(), it.summary.clean()) }
                .map { it.map { it.clean() } }
                .filter { it.all(String::isNotBlank) }
                .map { it.joinToString(del) }
                .alsoPrint { "Exporting ${it.size} Trials" }
                .forEach(out::println)
    }

    fun String.clean() = this
            .replace(Regex("[^a-zA-Z]+"), " ")
            .replace(del, " ")
            .replace("\"", "")
            .replace(Regex("\\s+"), " ")
            .trim()

}