package me.avo.clinical.trials.learning.komputation

import com.komputation.cpu.network.*
import com.komputation.cpu.workflow.*
import com.komputation.initialization.*
import com.komputation.instructions.continuation.activation.*
import com.komputation.instructions.continuation.convolution.*
import com.komputation.instructions.continuation.dropout.*
import com.komputation.instructions.continuation.projection.*
import com.komputation.instructions.continuation.stack.*
import com.komputation.instructions.entry.*
import com.komputation.optimization.*
import java.util.*

class SimpleNetwork(
    val batchSize: Int,
    val numberFilters: Int,
    val random: Random,
    val keepProbability: Float,
    val initialization: InitializationStrategy,
    val optimization: OptimizationInstruction,
    val embeddingDimension: Int,
    val filterWidth: Int,
    val filterHeight: Int
) : NetworkImplementation {

    override fun build(processedData: ProcessedData): Pair<Network, CpuTester> {
        val (embeddings, _, _, testRepresentations, testTargets, maximumDocumentLength, _, numberCategories) = processedData

        val network = network(
            batchSize,
            lookup(embeddings, maximumDocumentLength, embeddingDimension, optimization),
            stack(
                convolution(numberFilters, 2, filterHeight, initialization, optimization),
                convolution(numberFilters, 3, filterHeight, initialization, optimization)
            ),
            relu(),
            dropout(random, keepProbability),
            projection(numberCategories, initialization, initialization, optimization),
            softmax()
        )

        val test = network.test(testRepresentations, testTargets, batchSize, numberCategories, 1)

        return network to test
    }


}