package me.avo.clinical.trials

object TrialCombiner {

    val summaries = ClassLoader.loadSummaries()

    fun make(data: Map<String, List<String>>) = data
            .filter { summaries.containsKey(it.key) }
            .map { (id, keywords) ->
                Trial(id, keywords, summaries [id]!!)
            }.alsoPrint { "Was able to combine ${it.size} Trials" }

}