package me.avo.clinical.trials.komputation

import java.io.File

object TrialData {

    const val delimiter = "|"
    const val path = "trials.txt"

    fun load(size: Int): Pair<List<List<String>>, List<String>> {
        val lines = File(path)
                .bufferedReader()
                .lineSequence()
                .drop(1)
                .map { it.split(delimiter) }
                .take(size)
                .toList()

        val x = lines.map { it.drop(1).first().split(" ") }
        val y = lines.map { it.first() }
        return x to y
    }


}
