package me.avo.clinical.trials.ml

import me.avo.clinical.trials.Trial
import quickml.supervised.ensembles.randomForest.randomDecisionForest.RandomDecisionForestBuilder
import quickml.supervised.tree.decisionTree.DecisionTreeBuilder

class Classifier(val trials: List<Trial>) {


    fun run() {
        val data = trials.map { TrialClassifier(it) }

        val i = 1
        val training = data.drop(i)
        val test = data.take(i)

        val tree = RandomDecisionForestBuilder(DecisionTreeBuilder<TrialClassifier>()).buildPredictiveModel(training)


        val input = test.first().attributes

        val prediction = tree.predict(input)
        println("Prediction: $prediction")

    }

}