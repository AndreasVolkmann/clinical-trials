package me.avo.clinical.trials.komputation

import com.komputation.matrix.Matrix

data class ProcessedData(
        val embeddings: Array<FloatArray>,
        val trainingRepresentations: Array<Matrix>,
        val trainingTargets: Array<FloatArray>,
        val testRepresentations: Array<Matrix>,
        val testTargets: Array<FloatArray>,
        val maximumDocumentLength: Int,
        val numberCategories: Int
)