package me.avo.clinical.trials.ml

import me.avo.clinical.trials.Trial
import org.apache.spark.SparkConf
import org.apache.spark.api.java.JavaSparkContext
import org.apache.spark.sql.Encoders
import org.apache.spark.sql.SparkSession


class Classifier(val trials: List<Trial>) {

    val encoder = Encoders.bean(JavaSparkTrial::class.java)

    fun spark() {
        val data = trials.map {
            JavaSparkTrial().apply {
                label = it.keywords.first()
                summary = it.summary
            }
        }

        val conf = SparkConf()
                .setMaster("local")
                .setAppName("Clinical Trials")
        val sc = JavaSparkContext(conf)

        val spark = SparkSession
                .builder()
                .getOrCreate()


        val dataset = spark.createDataset(data, encoder)

        dataset.show(5)


    }

}