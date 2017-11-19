package me.avo.clinical.trials.komputation

import com.komputation.cpu.workflow.*
import com.komputation.initialization.*
import com.komputation.loss.*
import com.komputation.optimization.adaptive.*
import java.util.*
import kotlin.system.*

fun main(args: Array<String>) {
    Args.parse(args)
    if (Args.help) return
    run(Args.embeddingDimension)
}

fun run(embeddingDimension: Int) {
    val random = Random(1)
    val initialization = uniformInitialization(random, -0.1f, 0.1f)

    val optimization = adam()

    val batchSize = 32
    val hasFixedLength = false
    val numberIterations = 25

    val numberFilters = 100

    val filterHeight = embeddingDimension
    val filterWidth = 2
    val filterWidths = intArrayOf(2, 3)
    val maximumFilterWidth = filterWidths.max()!!
    val numberFilterWidths = filterWidths.size

    val keepProbability = 0.5f

    val size = 10_000
    val trainingSize = (size / 10) * 9
    val testSize = (size / 10)
    println("Loading data (size = $size)")
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

    try {
        val time = network
                .training(trainingRepresentations, trainingTargets, numberIterations, logisticLoss(numberCategories), afterEach)
                .train()
        println("Took ${time / 1000 / 60} minutes")
    } finally {
        updateScore(results, size, embeddingDimension, optimization)
    }
}

fun CpuTrainer.train(): Long = measureTimeMillis { run() }
