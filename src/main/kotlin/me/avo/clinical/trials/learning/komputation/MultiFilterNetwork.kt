package me.avo.clinical.trials.learning.komputation

import com.komputation.cpu.network.*
import com.komputation.cpu.workflow.*
import com.komputation.initialization.*
import com.komputation.layers.entry.*
import com.komputation.layers.forward.*
import com.komputation.layers.forward.activation.*
import com.komputation.layers.forward.convolution.*
import com.komputation.layers.forward.dropout.*
import com.komputation.layers.forward.projection.*
import com.komputation.optimization.*
import java.util.*

class MultiFilterNetwork(val batchSize: Int,
                         val hasFixedLength: Boolean,
                         val numberFilters: Int,
                         val random: Random,
                         val keepProbability: Float,
                         val initialization: UniformInitialization,
                         val optimization: OptimizationInstruction,
                         val embeddingDimension: Int,
                         val filterWidths: List<Int>,
                         val filterHeight: Int,
                         val numberFilterWidths: Int) : NetworkImplementation {

    override fun build(processedData: ProcessedData): Pair<Network, CpuTester> {
        val (embeddings, trainingRepresentations, trainingTargets,
                testRepresentations, testTargets,
                maximumDocumentLength, _, numberCategories
                ) = processedData
/*
        val network = Network(
                batchSize,
                lookupLayer(embeddings, maximumDocumentLength, hasFixedLength, embeddingDimension, optimization),
                concatenation(
                        *filterWidths.map { filterWidth ->
                            convolutionalLayer(embeddingDimension, maximumDocumentLength, hasFixedLength, numberFilters, filterWidth, filterHeight, initialization, initialization, optimization)
                        }.toTypedArray()
                ),
                reluLayer(numberFilterWidths * numberFilters),
                dropoutLayer(random, keepProbability, numberFilterWidths * numberFilters),
                projectionLayer(filterWidths.size * numberFilters, numberCategories, initialization, initialization, optimization),
                softmaxLayer(numberCategories)
        )

        val test = network.test(
                testRepresentations,
                testTargets,
                batchSize,
                numberCategories,
                1)

        return network to test*/
        TODO()
    }

}