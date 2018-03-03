package me.avo.clinical.trials.learning.komputation

import com.komputation.matrix.*

data class ProcessedData(
    val embeddings: Array<FloatArray>,
    val trainingRepresentations: Array<IntMatrix>,
    val trainingTargets: Array<FloatArray>,
    val testRepresentations: Array<IntMatrix>,
    val testTargets: Array<FloatArray>,
    val maximumDocumentLength: Int,
    val indexedCategories: Map<String, Int>,
    val numberCategories: Int
)