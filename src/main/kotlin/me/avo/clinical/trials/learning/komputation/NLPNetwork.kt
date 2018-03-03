package me.avo.clinical.trials.learning.komputation

import com.komputation.cpu.workflow.*
import com.komputation.initialization.*
import com.komputation.instructions.loss.*
import com.komputation.optimization.adaptive.*
import java.awt.*
import java.util.*

fun run(embeddingDimension: Int, iterations: Int, size: Int) {
    val random = Random(1)
    val initialization: InitializationStrategy = uniformInitialization(random, -0.1f, 0.1f)
    // uniformInitialization(random, -0.1f, 0.1f)

    val optimization = adam()

    val batchSize = 10
    val hasFixedLength = false

    val numberFilters = 100
    val filterHeight = embeddingDimension
    val filterWidth = 2
    val filterWidths = intArrayOf(2, 3)
    val maximumFilterWidth = filterWidths.max()!!
    val numberFilterWidths = filterWidths.size

    val keepProbability = 0.5f

    val trainingSize = (size / 10) * 9
    val testSize = (size / 10)
    println("Loading data (size = $size)")
    val (x, y) = TrialData.load(size)

    val trainingDocuments = x.take(trainingSize)
    val testDocuments = x.takeLast(testSize)

    println("Processing")
    println("Using Glove embedding dimension $embeddingDimension")
    val processedData =
        tokenize(y, trainingDocuments, testDocuments, maximumFilterWidth, trainingSize, testSize, embeddingDimension)
    val (embeddings, trainingRepresentations, trainingTargets, testRepresentations, testTargets, maximumDocumentLength, _, numberCategories) = processedData

    println("Starting network, $iterations iterations")
    val (network, test) = SimpleNetwork(
        batchSize, numberFilters, random, keepProbability, initialization, optimization,
        embeddingDimension, filterWidth, filterHeight
    ).build(processedData)


    val scoreWriter = ScoreWriter()
    val highScore: Float = scoreWriter.previousScore
    var results = -1 to 0.0f
    fun makeAfterEach(test: CpuTester, highScore: Float) = fun(i: Int, f: Float) {
        val score = test.run()
        if (score > results.second) {
            println("\tBetter score! ${results.second} > $score")
            results = i to score
        }
        if (score > highScore) {
            Toolkit.getDefaultToolkit().beep()
        }
        println("$i) $score - loss: $f")
    }

    val afterEach = makeAfterEach(test, highScore)

    var nextIterations = iterations
    var proceed = true
    var time = 0L

    try {
        while (proceed) {
            time += network
                .training(
                    trainingRepresentations, trainingTargets, nextIterations, logisticLoss(), afterEach
                )
                .run()
                .first
            println("Would you like to proceed training? (Best score so far: ${results.second}")
            println("Empty line = abort; Parsable Int = nextIterations")
            val input = readLine()
            if (input.isNullOrBlank()) proceed = false
            else input!!.toIntOrNull()?.let {
                println("Continuing with $it iterations ...")
                nextIterations = it
            }
        }
    } finally {
        println("Took ${time / 1000 / 60} minutes")
        scoreWriter.updateScore(results, size, embeddingDimension, optimization)
    }
}

