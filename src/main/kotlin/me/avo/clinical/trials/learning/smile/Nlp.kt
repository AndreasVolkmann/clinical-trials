package me.avo.clinical.trials.learning.smile

import me.avo.clinical.trials.processing.*
import smile.feature.*
import smile.nlp.dictionary.*
import smile.nlp.normalizer.*
import smile.nlp.stemmer.*
import smile.nlp.tokenizer.*
import java.io.*

fun main(args: Array<String>) {

    val data = File("trials.txt").useLines {
        it.drop(1).map { it.split("|") }.map { it[0] to it[1] }.toList()
    }.take(5000)

    val y = data.map { it.first }
    val x = data.map { it.second }

    val stemmer = LancasterStemmer()
    val stopwords = EnglishStopWords.COMPREHENSIVE

    val features = x
            .map(String::normalize)
            .map(String::tokenize)
            .also { it.take(1).forEach { it.printString() } }
            .map { it.map(stemmer::stem) }
            .also { it.take(1).forEach { it.printString() } }
            .also { it.printNumberOfWords() }
            .map { it.filterNot(stopwords::contains) }
            .also { it.printNumberOfWords() }
            .print(1)

    val bag = features.flatten().bag()
    val corpus = features.map { bag.feature(it) }

    val responseMap = y.mapIndexed { index, s -> s to index }.toMap()
    val headers = "response" + " " + bag.attributes().joinToString(" ") { it.name }
    File("trials-smile-vectorized.txt").printWriter().use { out ->
        out.println(headers)
        corpus.map { it.joinToString(" ") }.forEachIndexed { index, vectorized ->
            val response = responseMap[y[index]]
            out.println("$response $vectorized")
        }
    }

}

private fun List<List<*>>.printNumberOfWords() = sumBy { it.size }.let { println("Total: $it") }
private fun List<*>.printString() = println(joinToString())
private fun Array<*>.printString() = println(joinToString())
private fun DoubleArray.printString() = println(joinToString())

val tokenizer = SimpleTokenizer()
fun String.normalize(): String = SimpleNormalizer.getInstance().normalize(this)
fun String.splitSentence() = SimpleSentenceSplitter.getInstance().split(this)
fun String.tokenize(): Array<String> = tokenizer.split(this)

fun List<String>.bag() = Bag(toTypedArray())

inline fun <reified T> Bag<T>.feature(x: List<T>): DoubleArray = feature(x.toTypedArray())
