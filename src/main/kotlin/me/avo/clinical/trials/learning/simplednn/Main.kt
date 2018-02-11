package me.avo.clinical.trials.learning.simplednn

/*import Configuration
import com.kotlinnlp.simplednn.core.functionalities.activations.*
import com.kotlinnlp.simplednn.core.functionalities.losses.*
import com.kotlinnlp.simplednn.core.functionalities.outputevaluation.*
import com.kotlinnlp.simplednn.core.functionalities.updatemethods.adam.*
import com.kotlinnlp.simplednn.core.neuralnetwork.preset.*
import com.kotlinnlp.simplednn.core.neuralprocessor.feedforward.*
import com.kotlinnlp.simplednn.core.optimizer.*
import com.kotlinnlp.simplednn.dataset.*
import com.kotlinnlp.simplednn.helpers.training.*
import com.kotlinnlp.simplednn.helpers.validation.*
import com.kotlinnlp.simplednn.simplemath.ndarray.dense.*
import utils.*
import utils.exampleextractor.*
import java.nio.file.*

fun main(args: Array<String>) {

    val dataset = CorpusReader<SimpleExample<DenseNDArray>>().read(
            corpusPath = Configuration.loadFromFile(path = FileSystems.getDefault().getPath("D:/Dev/Java/clinical-trials/src/main/resources/configuration.yaml")).mnist.datasets_paths,
            exampleExtractor = ClassificationExampleExtractor(outputSize = 10),
            perLine = false
    )

    MNISTTest(dataset).start()
}

class MNISTTest(val dataset: Corpus<SimpleExample<DenseNDArray>>) {

    private val neuralNetwork = FeedforwardNeuralNetwork(
            inputSize = 784,
            hiddenSize = 500,
            hiddenActivation = ReLU(),
            outputSize = 10,
            outputActivation = Softmax())

    fun start() {
        train()
    }

    private fun train() {
        println("\n-- TRAINING")

        val optimizer = ParamsOptimizer(
                params = neuralNetwork.model,
                updateMethod = ADAMMethod(stepSize = 0.001, beta1 = 0.9, beta2 = 0.999))

        val trainingHelper = FeedforwardTrainingHelper<DenseNDArray>(
                neuralProcessor = FeedforwardNeuralProcessor(neuralNetwork),
                optimizer = optimizer,
                lossCalculator = SoftmaxCrossEntropyCalculator(),
                verbose = true)

        val validationHelper = FeedforwardValidationHelper<DenseNDArray>(
                neuralProcessor = FeedforwardNeuralProcessor(neuralNetwork),
                outputEvaluationFunction = ClassificationEvaluation())

        trainingHelper.train(
                trainingExamples = dataset.training,
                validationExamples = dataset.validation,
                epochs = 15,
                batchSize = 1,
                shuffler = Shuffler(enablePseudoRandom = true, seed = 1),
                validationHelper = validationHelper)
    }
}*/