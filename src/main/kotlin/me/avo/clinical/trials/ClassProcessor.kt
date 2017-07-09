package me.avo.clinical.trials

class ClassProcessor(val data: Map<String, List<String>>) {

    val flatKeys = data.flatMap { it.value }

    val distinct: Int = flatKeys.distinct().size
    val average: Double = data.map { it.value.size }.average()

    fun analyze() {
        println("There are $distinct distinct terms out of a total ${flatKeys.size}")
        println("Average per trial: $average")
    }

    fun search(term: String) = flatKeys
            .alsoPrint { "Containing '$term':" }
            .filter { it.contains(term, true) }
            .alsoPrint { formatPercent(it.size, flatKeys.size) }
            .groupBy { it }
            .mapValues { it.value.size }
            .map { it.value to it.key }.toMap()
            .toSortedMap(compareByDescending { it })
            .printTen()


    fun trimToAverage(): Map<String, List<String>> {
        // order by appearance
        val sizeMap = flatKeys
                .groupBy { it }
                .map { (label, list) -> label to list.size }
                .toMap()

        return data
                .mapValues { (id, labels) ->
                    labels.sortedByDescending { sizeMap[it] }
                            .take(average.toInt())
                }
                .alsoPrint { "Final size: ${it.size}. Truncated ${data.size - it.size}" }
    }

}