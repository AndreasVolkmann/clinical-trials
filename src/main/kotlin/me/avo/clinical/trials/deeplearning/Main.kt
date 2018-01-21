package me.avo.clinical.trials.deeplearning

import org.deeplearning4j.datasets.iterator.impl.*
import org.deeplearning4j.eval.*
import org.deeplearning4j.nn.api.*
import org.deeplearning4j.nn.multilayer.*
import org.deeplearning4j.nn.weights.*
import org.deeplearning4j.optimize.listeners.*
import org.nd4j.linalg.activations.*
import org.nd4j.linalg.learning.config.*
import org.nd4j.linalg.lossfunctions.LossFunctions.LossFunction

fun main(args: Array<String>) {

    val numRows = 28
    val numColumns = 28
    val outputNum = 10 // number of output classes
    val batchSize = 128 // batch size for each epoch
    val rngSeed = 123 // random number seed for reproducibility
    val numEpochs = 15 // number of epochs to perform
    val rate = 0.0015

    val mnistTrain = MnistDataSetIterator(batchSize, true, rngSeed)
    val mnistTest = MnistDataSetIterator(batchSize, false, rngSeed)

    val conf = multiLayerConfiguration {
        seed(rngSeed)
        optimizationAlgo = OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT
        numIterations = 1
        activation(Activation.RELU)
        weightInit = WeightInit.XAVIER
        learningRate = rate
        iUpdater = Nesterovs(0.98)
        isUseRegularization = true
        l2 = rate * 0.005

        list {
            denseLayer(0) {
                nIn(numRows * numColumns)
                nOut(500)
            }
            denseLayer(1) {
                nIn(500)
                nOut(100)
            }
            outputLayer(2, LossFunction.NEGATIVELOGLIKELIHOOD) {
                nIn(100)
                nOut(outputNum)
                activation(Activation.SOFTMAX)
            }
            pretrain(false)
            backprop(true)
        }
    }

    MultiLayerNetwork(conf).also { model ->
        model.init()
        model.setListeners(ScoreIterationListener(50))

        println("Train model")
        for (i in 0 until numEpochs) {
            model.fit(mnistTrain)
        }

        println("Evaluate model")
        val eval = Evaluation(outputNum)

        while (mnistTest.hasNext()) {
            val next = mnistTest.next()
            val output = model.output(next.featureMatrix)
            eval.eval(next.labels, output)
        }

        println(eval.stats())
        println("Done")
    }
}