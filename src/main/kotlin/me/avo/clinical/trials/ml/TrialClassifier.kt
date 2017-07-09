package me.avo.clinical.trials.ml

import me.avo.clinical.trials.Trial
import quickml.data.AttributesMap
import quickml.data.instances.ClassifierInstance

class TrialClassifier(trial: Trial) : ClassifierInstance(
        AttributesMap(mapOf("summary" to trial.summary)),
        trial.keywords.first()
)