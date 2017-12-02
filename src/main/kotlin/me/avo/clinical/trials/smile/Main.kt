package me.avo.clinical.trials.smile

import smile.classification.*
import smile.data.*
import smile.data.parser.*
import smile.math.*
import smile.math.kernel.*
import smile.plot.*
import java.awt.*
import java.awt.Color.*

fun main(args: Array<String>) {

    val data = DelimitedTextParser().run {
        setResponseIndex(NominalAttribute("class"), 0)
        parse("trials-converted.txt")
    }

    val size = 3000

    val train = data.head(size / 10 * 9)
    val test = data.tail(size / 10)

    val (x, y) = train.getXY()
    val (testX, testY) = test.getXY()

    val svm = SVM(GaussianKernel(0.7), 5.0, 5, SVM.Multiclass.ONE_VS_ALL)
    svm.learn(x, y)
    svm.finish()

    val error = testX.indices.count { svm.predict(testX[it]) != testY[it] }
    println("USPS error rate = ${100.0 * error / testX.size}")

    ScatterPlot.plot(x.map { it.slice(0..2).toDoubleArray() }.toTypedArray(), y, CharArray(5, Int::toChar), arrayOf(BLACK , BLUE, GRAY, GREEN, ORANGE))
            .show()


    /*
    println("USPS one more epoch...")
    for (aX in x) {
        val j = Math.randomInt(x.size)
        svm.learn(x[j], y[j])
    }

    svm.finish()

    error = testX.indices.count { svm.predict(testX[it]) != testY[it] }
    println("USPS error rate = ${100.0 * error / testX.size}")*/

}

fun AttributeDataset.getX(): Array<out DoubleArray> = toArray(Array(size()) { DoubleArray(0) })
fun AttributeDataset.getY(): IntArray = toArray(IntArray(size()))
fun AttributeDataset.getXY() = getX() to getY()
