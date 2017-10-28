package me.avo.clinical.trials.komputation

import com.komputation.cpu.workflow.CpuTrainer
import com.komputation.initialization.uniformInitialization
import com.komputation.loss.logisticLoss
import com.komputation.optimization.stochasticGradientDescent
import java.io.File
import java.util.*
import kotlin.system.measureTimeMillis

fun main(args: Array<String>) {
    Args.parse(args)
    if (Args.help) return
    run(Args.embeddingDimension)
}

fun run(embeddingDimension: Int) {
    val random = Random(1)
    val initialization = uniformInitialization(random, -0.1f, 0.1f)

    val optimization = stochasticGradientDescent(0.001f)

    val batchSize = 32
    val hasFixedLength = false
    val numberIterations = 50

    val numberFilters = 100

    val filterHeight = embeddingDimension
    val filterWidth = 2
    val filterWidths = intArrayOf(2, 3)
    val maximumFilterWidth = filterWidths.max()!!
    val numberFilterWidths = filterWidths.size

    val keepProbability = 0.5f

    val size = 9630
    val trainingSize = (size / 10) * 9
    val testSize = (size / 10)
    println("Loading data")
    val (x, y) = TrialData.load(size)

    val trainingDocuments = x.take(trainingSize)
    val testDocuments = x.takeLast(testSize)

    println("Processing")
    println("Using Glove embedding dimension $embeddingDimension")
    val processedData = tokenize(y, trainingDocuments, testDocuments, maximumFilterWidth, trainingSize, testSize, embeddingDimension)
    val (embeddings, trainingRepresentations, trainingTargets, testRepresentations, testTargets, maximumDocumentLength, numberCategories) = processedData

    println("Starting network, $numberIterations iterations")
    val (network, test) =
            SimpleNetwork(batchSize, hasFixedLength, numberFilters, random, keepProbability, initialization, optimization, embeddingDimension, filterWidth, filterHeight)
                    .build(processedData)

    var results = -1 to 0.0f
    val afterEach = { i: Int, f: Float ->
        val score = test.run()
        if (score > results.second) {
            results = i to score
        }
        println("$i) $score")
    }

    val time = network
            .training(trainingRepresentations, trainingTargets, numberIterations, logisticLoss(numberCategories), afterEach)
            .train()

    println("Took ${time / 1000 / 60} minutes")
    updateScore(results, size)
}

fun CpuTrainer.train(): Long = measureTimeMillis { run() }

fun updateScore(results: Pair<Int, Float>, size: Int) = results.let { (iteration, score) ->
    println("Best score: $results")
    val delimiter = "\t"
    val scoreFile = File("score.txt")
    val exists = scoreFile.exists()
    val lines: List<String> = if (exists) scoreFile.readLines().drop(1) else listOf()

    val previousScore = if (exists && lines.isNotEmpty()) {
        lines.first().split(delimiter).first().toFloat()
    } else 0.0f

    if (score > previousScore) {
        println("New High score!")
        val headers = listOf("Score", "Iteration", "Size")
        val line = listOf(score, iteration, size)
        val converted = listOf(headers, line).map { it.joinToString(delimiter) }
        val linesToWrite = converted + lines
        scoreFile.printWriter().use { out ->
            linesToWrite.forEach(out::println)
        }
    }
}