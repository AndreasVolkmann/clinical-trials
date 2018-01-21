package me.avo.clinical.trials.deeplearning

import org.deeplearning4j.models.word2vec.*
import org.deeplearning4j.nn.multilayer.*
import org.deeplearning4j.text.sentenceiterator.*
import org.deeplearning4j.text.tokenization.tokenizer.preprocessor.*
import org.deeplearning4j.text.tokenization.tokenizerfactory.*
import org.nd4j.linalg.factory.*
import java.io.*

fun main(args: Array<String>) {
    println("Building model")

    val vec = word2Vec {
        minWordFrequency(5)
        iterations(1)
        layerSize(100)
        seed(42)
        windowSize(5)
        // LabelAwareListSentenceIterator(File("trials.txt").inputStream(), "|")
        iterator = BasicLineIterator(File("trials.txt")).apply {
            preProcessor = SentencePreProcessor {
                it.split("|")[1]
            }
        }
        tokenizerFactory(DefaultTokenizerFactory().apply {
            tokenPreProcessor = CommonPreprocessor()
        })
    }

    println("Fitting Word2Vec model")
    vec.fit()

    println("Writing word vectors to file")

    val closeWord = "cancer"
    println("Closest Words to $closeWord")
    vec.wordsNearestSum(closeWord, 10).let {
        println(it)
    }


    applyVector(vec, TODO())
}

fun applyVector(vec: Word2Vec, model: MultiLayerNetwork) {
    val vectorSize = vec.getWordVector(vec.vocab().wordAtIndex(0)).size
    Nd4j.create(1, vectorSize, 2)
    //model.fit(vec)
}
