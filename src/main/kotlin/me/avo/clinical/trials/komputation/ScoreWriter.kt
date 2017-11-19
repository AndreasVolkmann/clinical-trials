package me.avo.clinical.trials.komputation

import com.komputation.optimization.*
import java.io.*

fun updateScore(results: Pair<Int, Float>, size: Int, embeddingDimension: Int, optimization: OptimizationInstruction) = results.let { (iteration, score) ->
    println("Best score: $results")
    val scoreFile = File(scoreFileName)
    val exists = scoreFile.exists()
    val lines: List<String> = if (exists) scoreFile.readLines().drop(1) else listOf()

    val previousScore = if (exists && lines.isNotEmpty()) {
        lines.first().split(delimiter).first().toFloat()
    } else 0.0f

    if (score > previousScore) {
        println("New high score!")
        val line = listOf(score, iteration, size, embeddingDimension, optimization::class.simpleName)
        val converted = listOf(headers, line).map { it.joinToString(delimiter) }
        val linesToWrite = converted + lines
        scoreFile.printWriter().use { out ->
            linesToWrite.forEach(out::println)
        }
    } else println("No new high score.")
}

private val scoreFileName = "score.tsv"
private val delimiter = "\t"
private val headers = listOf("Score", "Iteration", "Size", "Dimension", "Optimization")
