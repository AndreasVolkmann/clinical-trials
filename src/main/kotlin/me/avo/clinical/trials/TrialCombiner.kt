package me.avo.clinical.trials

import java.io.File

object TrialCombiner {

<<<<<<< HEAD
    const val del = "|"
    val summaries = ClassLoader.loadSummaries()

    fun make(data: Map<String, List<String>>): List<Trial> = data
            .filter { summaries.containsKey(it.key) }
            .map { (id, keywords) -> Trial(id, keywords, summaries[id]!!) }
            .alsoPrint { "Was able to combine ${it.size} Trials" }

=======
    val del = "|"
    val summaries = ClassLoader.loadDetailedSummaries()
            .filterValues {
                !it.contains("See above", true)
                        && !it.contains("brief summary", true)
                        && !it.contains("see summary", true)
            }


    fun make(data: Map<String, List<String>>): List<Trial> = data
            .filter { summaries.containsKey(it.key) }
            .map { (id, keywords) -> makeTrial(id, keywords) }
            // TODO better analysis of filtering
            .alsoPrint { "Was able to combine ${it.size} Trials" }
>>>>>>> bcc566020a40c5230c91aa02b7dc6e9aac9481a0

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
            .filter { summaries.containsKey(it.key) }
            .map { (id, keywords) -> makeTrial(id, keywords) }


    fun makeTrial(id: String, keywords: List<String>)
            = Trial(id, keywords, summaries[id]!!.filterWords(keywords).clean())

}