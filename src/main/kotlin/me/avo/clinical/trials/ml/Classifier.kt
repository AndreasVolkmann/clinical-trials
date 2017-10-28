package me.avo.clinical.trials.ml

import me.avo.clinical.trials.Trial
import org.apache.spark.SparkConf
import org.apache.spark.ml.Pipeline
import org.apache.spark.ml.classification.LogisticRegression
import org.apache.spark.ml.classification.LogisticRegressionModel
import org.apache.spark.ml.classification.NaiveBayes
import org.apache.spark.ml.feature.HashingTF
import org.apache.spark.ml.feature.StopWordsRemover
import org.apache.spark.ml.feature.Tokenizer
import org.apache.spark.sql.Encoders
import org.apache.spark.sql.SparkSession

class Classifier {

    val conf = SparkConf()
            .setMaster("local")
            .setAppName("Clinical Trials")
            //.set("driver.memory", "4g")
            .setExecutorEnv("memory", "6g")

    val encoder = Encoders.bean(JavaSparkTrial::class.java)

    fun spark(trials: List<Trial>) {
        val hashStore = mutableMapOf<Int, String>()
        val spark = SparkSession
                .builder()
                .config(conf)
                .getOrCreate()


        fun List<JavaSparkTrial>.toDataSet() = spark.createDataset(this, encoder)
        fun toJavaTrial(trial: Trial) = JavaSparkTrial().apply {
            val hash = trial.keywords.first().hashCode()
            hashStore.put(hash, trial.keywords.first())
            label = hash
            summary = trial.summary
        }

        val data = trials.map { toJavaTrial(it) }
        val train = data.take(1000)
        val test = data.takeLast(250)


        //val model = pipeline.fit(train.toDataSet())
        val model = prepare(spark, train)
        val predictions = model.transform(test.toDataSet())
        predictions.select("final-label", "probability", "prediction", "features").collectAsList().forEach {
            println("${it.get(0)} - ${it.get(1)}: ${it.get(2)} (${it.get(3)})")
        }
    }


    val tokenizer = Tokenizer()
            .setInputCol("summary")
            .setOutputCol("words")
    val remover = StopWordsRemover()
            .setInputCol(tokenizer.outputCol)
            .setOutputCol("final")
    val hashingTF = HashingTF()
            .setNumFeatures(1000)
            .setInputCol(remover.outputCol)
            .setOutputCol("features")
    val model = NaiveBayes()

    val pipeline = Pipeline()
            .setStages(arrayOf(tokenizer, remover, hashingTF, model))


    fun prepare(spark: SparkSession, data: List<JavaSparkTrial>) = spark.createDataset(data, encoder)
            .let { tokenizer.transform(it).drop(tokenizer.inputCol).apply { show(5) } }
            .let { remover.transform(it).drop(tokenizer.outputCol).apply { show(5) } }
            .let { hashingTF.transform(it).drop(remover.outputCol) }
            .let(model::fit)


}