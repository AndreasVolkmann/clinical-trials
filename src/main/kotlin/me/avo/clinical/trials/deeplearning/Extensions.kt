package me.avo.clinical.trials.deeplearning

import org.datavec.api.transform.*
import org.datavec.api.transform.schema.*
import org.deeplearning4j.models.word2vec.*
import org.deeplearning4j.nn.conf.*
import org.deeplearning4j.nn.conf.layers.*
import org.deeplearning4j.text.sentenceiterator.*
import org.nd4j.linalg.lossfunctions.*

fun neuralNetConfiguration(block: NeuralNetConfiguration.Builder.() -> Unit): NeuralNetConfiguration {
    return NeuralNetConfiguration.Builder().apply(block).build()
}

fun multiLayerConfiguration(block: NeuralNetConfiguration.Builder.() -> ListBuilder): MultiLayerConfiguration {
    return NeuralNetConfiguration.Builder().run { block(this).build() }
}

fun NeuralNetConfiguration.Builder.list(block: ListBuilder.() -> Unit): NeuralNetConfiguration.ListBuilder {
    return list().apply(block)
}

typealias ListBuilder = NeuralNetConfiguration.ListBuilder

fun ListBuilder.denseLayer(ind: Int, block: DenseLayer.Builder.() -> DenseLayer.Builder): ListBuilder {
    layer(ind, denseLayer(block))
    return this
}

fun ListBuilder.outputLayer(ind: Int, lossFunction: LossFunctions.LossFunction, block: OutputLayer.Builder.() -> OutputLayer.Builder): ListBuilder {
    layer(ind, outputLayer(lossFunction, block))
    return this
}

fun denseLayer(block: DenseLayer.Builder.() -> DenseLayer.Builder): DenseLayer {
    return block(DenseLayer.Builder()).build()
}

fun outputLayer(lossFunction: LossFunctions.LossFunction, block: OutputLayer.Builder.() -> OutputLayer.Builder): OutputLayer {
    return block(OutputLayer.Builder(lossFunction)).build()
}

fun ListBuilder.layer(ind: Int, block: () -> Layer): ListBuilder {
    layer(ind, block())
    return this
}


/*
NLP
 */

fun word2Vec(block: Word2Vec.Builder.() -> Unit): Word2Vec = Word2Vec.Builder().apply(block).build()

var Word2Vec.Builder.iterator: SentenceIterator
    get() = TODO()
    set(value) {
        iterate(value)
    }

/*
DataVec
 */

fun schema(block: Schema.Builder.() -> Unit): Schema = Schema.Builder().apply(block).build()

fun transformProcess(inputSchema: Schema, block: TransformProcess.Builder.() -> Unit): TransformProcess
        = TransformProcess.Builder(inputSchema).apply(block).build()