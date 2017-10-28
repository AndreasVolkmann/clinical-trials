package me.avo.clinical.trials.komputation

import com.komputation.cpu.network.Network
import com.komputation.cpu.workflow.CpuTester
import com.komputation.initialization.UniformInitialization
import com.komputation.layers.entry.lookupLayer
import com.komputation.layers.forward.activation.reluLayer
import com.komputation.layers.forward.activation.softmaxLayer
import com.komputation.layers.forward.convolution.convolutionalLayer
import com.komputation.layers.forward.dropout.dropoutLayer
import com.komputation.layers.forward.projection.projectionLayer
import com.komputation.optimization.OptimizationInstruction
import java.util.*

class SimpleNetwork(val batchSize: Int,
                    val hasFixedLength: Boolean,
                    val numberFilters: Int,
                    val random: Random,
                    val keepProbability: Float,
                    val initialization: UniformInitialization,
                    val optimization: OptimizationInstruction,
                    val embeddingDimension: Int,
                    val filterWidth: Int,
                    val filterHeight: Int
) : NetworkImplementation {

    override fun build(processedData: ProcessedData): Pair<Network, CpuTester> {
        val (embeddings, trainingRepresentations, trainingTargets,
                testRepresentations, testTargets,
                maximumDocumentLength, numberCategories
                ) = processedData

        val network = Network(
                batchSize,
                lookupLayer(embeddings, maximumDocumentLength, hasFixedLength, embeddingDimension, optimization),
                convolutionalLayer(embeddingDimension, maximumDocumentLength, hasFixedLength, numberFilters, filterWidth, filterHeight, initialization, initialization, optimization),
                reluLayer(numberFilters),
                dropoutLayer(random, keepProbability, numberFilters),
                projectionLayer(numberFilters, numberCategories, initialization, initialization, optimization),
                softmaxLayer(numberCategories)
        )

        val test = network
                .test(
                        testRepresentations,
                        testTargets,
                        batchSize,
                        numberCategories,
                        1)

        return network to test
    }


}