package me.avo.clinical.trials.learning.deeplearning

import krangl.*
import org.apache.commons.csv.*
import org.datavec.api.transform.*
import java.io.*

fun main(args: Array<String>) {

    val inputSchema = schema {
        val categories = arrayOf("Obesity", "Pain", "Breast Cancer", "Pharmacokinetics", "Cancer")
        addColumnCategorical("Label", *categories)
        addColumnsString("Summary")
    }

    val tp = transformProcess(inputSchema) {
        categoricalToInteger("Label")
    }

    val df = DataFrame.fromCSV(
            File("trials.txt"),
            CSVFormat.DEFAULT.withFirstRecordAsHeader().withDelimiter('|'))

    val result = df.transform(tp)

    result.glimpse()
    result.head(5).print(true)


}

fun DataFrame.transform(tp: TransformProcess): DataFrame {
    val transforms = tp.actionList.map { it.transform }
    val lookup = cols.associate { col -> col.name to transforms.filter { it.columnName() == col.name } }
    return transmute(
            *cols.map { col ->
                val transform = lookup[col.name]
                if (transform != null && transform.isNotEmpty()) {
                    col.name to { it[col.name].map(transform::apply) }
                } else {
                    col.name to { it[col.name] }
                }
            }.toTypedArray()
    )
}

fun List<Transform>.apply(target: Any): Any = fold(target) { acc, transform -> transform.map(acc) }


/*
val outputSchema = tp.finalSchema
println(outputSchema)

val conf = SparkConf().apply {
    setMaster("local[*]")
    setAppName("DataVec Example")
}
val sc = JavaSparkContext(conf)
val path = File("trials.txt").absolutePath
val stringData: JavaRDD<String> = sc.textFile(path)

val reader = CSVRecordReader(1, '|')
val parsedInputData = stringData.map(StringToWritablesFunction(reader))
val processedData = SparkTransformExecutor.execute(parsedInputData, tp)
val processedAsString = processedData.map(WritablesToStringFunction(","))

processedAsString.take(10).forEach(::println)
*/


