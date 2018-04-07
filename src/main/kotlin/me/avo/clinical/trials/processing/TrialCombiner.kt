package me.avo.clinical.trials.processing

import java.io.File

object TrialCombiner {

    const val del = "|"
    //val summaries = ClassLoader.loadSummaries()

    fun make(data: Map<String, List<String>>): List<Trial> = data
        .filter { summaries.containsKey(it.key) }
        .map { (id, keywords) -> Trial(id, keywords, title = "", summary = summaries[id]!!, conditions = listOf()) }
        .alsoPrint { "Was able to combine ${it.size} Trials" }

    val summaries = ClassLoader.loadSummaries()
        .mapValues { it.value.toLowerCase() }
        .filterValues { !it.contains("see above") && !it.contains("brief summary") && !it.contains("see summary") }

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

    fun cleanText(text: String) = text.clean()

    fun makeRaw(data: Map<String, List<String>>) = data
        .filterKeys(summaries::containsKey)
        .map { (id, keywords) -> makeTrial(id, keywords) }

    fun makeTrial(id: String, keywords: List<String>) = Trial(
        id = id,
        keywords = keywords,
        title = "",
        summary = summaries[id]!!.filterWords(keywords).clean(),
        conditions = listOf()
    )

}