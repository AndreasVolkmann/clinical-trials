package me.avo.clinical.trials.komputation

import com.komputation.demos.trec.NLP
import java.io.File

fun tokenize(y: List<String>,
             trainingDocuments: Iterable<List<String>>,
             testDocuments: Iterable<List<String>>,
             maximumFilterWidth: Int,
             trainingSize: Int,
             testSize: Int,
             embeddingDimension: Int): ProcessedData {

    val vocabulary = NLP.generateVocabulary(trainingDocuments)

    val embeddingFilePath: String = "D:\\Dev\\data\\glove\\glove.6B.${embeddingDimension}d.txt"
    val embeddingFile = File(embeddingFilePath)

    val embeddingMap = NLP.embedVocabulary(vocabulary, embeddingFile)
    val embeddableVocabulary = embeddingMap.keys.sorted()
    //val missing = vocabulary - embeddingMap.keys

    val trainingDocumentsWithFilteredTokens = NLP.filterTokens(trainingDocuments, embeddableVocabulary)
    val maximumDocumentLength = trainingDocumentsWithFilteredTokens.maxBy { document -> document.size }!!.size

    val testDocumentsWithFilteredTokens = NLP.filterTokens(testDocuments, embeddableVocabulary)

    val embeddableTrainingIndices = NLP.filterDocuments(trainingDocumentsWithFilteredTokens, maximumFilterWidth)
    val embeddableTestIndices = NLP.filterDocuments(testDocumentsWithFilteredTokens, maximumFilterWidth)

    val embeddableTrainingDocuments = trainingDocumentsWithFilteredTokens.slice(embeddableTrainingIndices)
    val embeddableTestDocuments = testDocumentsWithFilteredTokens.slice(embeddableTestIndices)

    val trainingRepresentations = NLP.vectorizeDocuments(embeddableTrainingDocuments, embeddableVocabulary)
    val testRepresentations = NLP.vectorizeDocuments(embeddableTestDocuments, embeddableVocabulary)

    val trainingCategories = y.take(trainingSize)
    val testCategories = y.takeLast(testSize)

    val embeddableTrainingCategories = trainingCategories.slice(embeddableTrainingIndices)
    val embeddableTestCategories = testCategories.slice(embeddableTestIndices)

    val indexedCategories = NLP.indexCategories(embeddableTrainingCategories.toSet())
    val numberCategories = indexedCategories.size

    println("Number of categories: $numberCategories")
    println("Category map: $indexedCategories")

    val trainingTargets = NLP.createTargets(embeddableTrainingCategories, indexedCategories)
    val testTargets = NLP.createTargets(embeddableTestCategories, indexedCategories)

    val embeddings = embeddableVocabulary
            .map { token -> embeddingMap[token]!! }
            .toTypedArray()

    return ProcessedData(embeddings,
            trainingRepresentations, trainingTargets,
            testRepresentations, testTargets,
            maximumDocumentLength, indexedCategories, numberCategories)
}