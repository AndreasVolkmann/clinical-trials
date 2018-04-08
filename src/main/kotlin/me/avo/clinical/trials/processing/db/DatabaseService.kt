package me.avo.clinical.trials.processing.db

import me.avo.clinical.trials.processing.Trial
import me.avo.clinical.trials.processing.alsoPrint
import me.avo.clinical.trials.processing.removeNewlineTrimSpaces
import java.io.File

class DatabaseService {

    private val trialWithTitleFile = File("trials_with_title.psv")
    private val trialsCombinedText = File("trials_combined_text.psv")
    private val cleanedTrialFile = File("trials_with_title_clean.psv")

    fun readTrialsFromFile() = trialWithTitleFile.useLines {
        cleanedTrialFile.printWriter().use { out ->
            out.println(headers)
            it
                .map { it.split("|") }
                .map { Trial(it[0], listOf(it[1]), it[2], it[3], listOf()) }
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


        //val fileSizeMap = listOf(trialWithTitleFile, trialsCombinedText)
        val trialTitleSizePre = getFileSize(trialWithTitleFile)
        val trialCombinedSizePre = getFileSize(trialsCombinedText)

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

        trialTitleSizePre?.compareSize(trialWithTitleFile)
        trialCombinedSizePre?.compareSize(trialsCombinedText)
    }

    private val headers = listOf("Id", "Keyword", "Title", "Summary").join()

    private fun getFileSize(file: File): Long? = if (file.exists()) file.length() / 1024 else null

    private fun Long.compareSize(other: File) = getFileSize(other)?.let {
        println("${other.name} size difference: ${it - this}")
    }

}

private const val delimiter = "|"
private fun List<String>.join() = joinToString(delimiter)
private fun Trial.toLine() = listOf(id, keywords.first(), title, summary).join()
private fun Trial.toCombinedLine() = listOf(keywords.first(), "$title $summary ${conditions.joinToString(" ")}")
    .map(String::clean)
    .join()

fun String.clean() = replace(delimiter, " ")
