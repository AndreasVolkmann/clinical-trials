package me.avo.clinical.trials.tensor

import org.tensorflow.Graph
import org.tensorflow.Session
import org.tensorflow.Tensor
import org.tensorflow.TensorFlow

fun main(args: Array<String>) {

    version()




}




fun version() {
    val g = Graph()

    val value = "Hello from ${TensorFlow.version()}"

    // Construct the computation graph with a single operation, a constant
    // named "MyConst" with a value "value".
    val t = Tensor.create(value.toByteArray())
    // The Java API doesn't yet include convenience functions for adding operations.
    g.opBuilder("Const", "MyConst").setAttr("dtype", t.dataType()).setAttr("value", t).build()

    val s = Session(g)
    val output = s.runner().fetch("MyConst").run()[0]
    println(String(output.bytesValue()))
}