package me.avo.clinical.trials.learning.komputation

import me.avo.clinical.trials.processing.*
import java.io.*

fun main(args: Array<String>) {
    val (embeddings, trainingRepresentations, trainingTargets, testRepresentations, testTargets, maximumDocumentLength, indexedCategories, numberCategories) = obtainData()
    val categories = indexedCategories.map { it.value to it.key }.toMap()
    println("Max doc length: $maximumDocumentLength")

    val features = embeddings.also {
        //it.map(FloatArray::size).average().let { println("Average size of features: $it") }
    }

    val response = trainingTargets
            .map { it.indexOf(1.0f) }
            .print(10)

    response.map { categories[it] }.also { textRepresentation ->
        features.take(10).forEachIndexed { i, f ->
            val clazz = textRepresentation[i]
            println("Feature $i is $clazz")
        }
    }

    val rows = response.mapIndexed { index, res ->
        val feature = features[index].join()
        "$res $feature"
    }

    File("trials-converted.txt").printWriter().use { out ->
        rows.forEach(out::println)
    }

}

private val size = 10_000
fun obtainData(): ProcessedData {
    val maximumFilterWidth = 3
    val embeddingDimension = 50

    val trainingSize = (size / 10) * 9
    val testSize = (size / 10)
    println("Loading data (size = $size)")
    val (x, y) = TrialData.load(size)

    val trainingDocuments = x.take(trainingSize)
    val testDocuments = x.takeLast(testSize)

    println("Processing")
    println("Using Glove embedding dimension $embeddingDimension")
    return tokenize(y, trainingDocuments, testDocuments, maximumFilterWidth, trainingSize, testSize, embeddingDimension)
}

private fun IntArray.join() = joinToString(" ")
private fun FloatArray.join() = joinToString(" ")
private fun List<Any>.join() = joinToString(" ")