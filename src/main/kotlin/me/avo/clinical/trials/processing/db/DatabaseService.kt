package me.avo.clinical.trials.processing.db

import me.avo.clinical.trials.processing.*
import java.io.*

class DatabaseService {

    private val trialWithTitleFile = File("trials_with_title.psv")
    private val trialsCombinedText = File("trials_combined_text.psv")
    private val cleanedTrialFile = File("trials_with_title_clean.psv")

    fun readTrialsFromFile() = trialWithTitleFile.useLines {
        cleanedTrialFile.printWriter().use { out ->
            out.println(headers)
            it
                .map { it.split("|") }
                .map { Trial(it[0], listOf(it[1]), it[2], it[3]) }
                .map { it.copy(summary = it.summary.removeNewlineTrimSpaces()) }
                .map(Trial::toLine)
                .forEach(out::println)
        }
    }

    fun fetchTrialsFromDb(limit: Int) {
        val trials = TrialDatabase().run {
            connect()
            val keywords = getTopKeywords(limit).alsoPrint { "Keywords: $it" }
            getTrials(keywords)
        }.alsoPrint { "Found ${it.size} Trials" }

        trialWithTitleFile.printWriter().use { out ->
            val lines = trials.map(Trial::toLine)
            out.println(headers)
            lines.forEach(out::println)
        }
        trialsCombinedText.printWriter().use { out ->
            out.println(listOf("Label", "Text").join())
            trials
                .map(Trial::toCombinedLine)
                .forEach(out::println)
        }
    }

    val headers = listOf("Id", "Keyword", "Title", "Summary").join()

}

private fun List<String>.join() = joinToString("|")
private fun Trial.toLine() = listOf(id, keywords.first(), title, summary).join()
private fun Trial.toCombinedLine() = listOf(keywords.first(), "$title $summary").join()