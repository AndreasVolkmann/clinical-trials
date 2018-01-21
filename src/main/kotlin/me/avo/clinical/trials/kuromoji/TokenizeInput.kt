package me.avo.clinical.trials.kuromoji

import com.atilika.kuromoji.ipadic.*
import me.avo.clinical.trials.*

fun main(args: Array<String>) {
    TestTokenizer(false).run {
        val wordsToAnalyze = listOf<String>("米", "天丼", "食べ物", "天ぷら")
        //wordsToAnalyze.forEach(this::analyzeWord)

        getTestWords()
                .groupBy { it }
                .mapValues { it.value.size }
                .forEach(::println)
    }
}

class TestTokenizer(includeVerbs: Boolean) {
    private val input = Trial::class.java.classLoader.getResource("jp_sample.txt").readText()
    private val tokenizer = Tokenizer()

    val filters = listOf<(Token) -> Boolean>(
            { it.partOfSpeechLevel1 == "名詞" || (includeVerbs && it.partOfSpeechLevel1 == "動詞") },
            //{ it.partOfSpeechLevel1 !in listOf("接続詞", "記号") },
            { it.partOfSpeechLevel2 == "一般" || it.partOfSpeechLevel2 == "自立" }
    )

    fun getTestWords(): List<String> = tokenizer
            .tokenize(input)
            .filter { token -> filters.all { it.invoke(token) } }
            .map { it.baseForm }

    fun analyzeWord(word: String) {
        tokenizer.tokenize(word).first().let {
            println(it)
            println(it.partOfSpeechLevel1)
            println(it.partOfSpeechLevel2)
            println(it.partOfSpeechLevel3)
            println(it.partOfSpeechLevel4)
        }
    }

}