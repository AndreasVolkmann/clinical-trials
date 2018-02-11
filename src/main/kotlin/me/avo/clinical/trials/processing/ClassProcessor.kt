package me.avo.clinical.trials.processing

class ClassProcessor(val data: Map<String, List<String>>) {

    val flatKeys = data.flatMap { it.value }

    val distinct: Int = flatKeys.distinct().size
    val average: Double = data.map { it.value.size }.average()

    fun analyze() {
        println("There are $distinct distinct terms out of a total ${flatKeys.size}")
        println("Average per trial: $average")
    }

    fun findCommon(amount: Int = 10) = flatKeys.groupBy { it }
            .values
            .sortedByDescending { it.size }
            .map { it.first() to it.size }
            .also {
                it.take(amount).mapIndexed { i, pair -> pair.apply { println("${i + 1}) $pair") } }
                        .sumBy { it.second }
                        .alsoPrint { "Combined size: $it" }
            }

    fun filterByCommon(top: Int) = findCommon(top)
            .take(top)
            .map { it.first }
            .let { keys -> data.filterValues { it.any(keys::contains) } }

    fun search(term: String) = flatKeys
            .alsoPrint { "Containing '$term':" }
            .filter { it.contains(term, true) }
            .alsoPrint { formatPercent(it.size, flatKeys.size) }
            .groupBy { it }
            .mapValues { it.value.size }
            .map { it.value to it.key }.toMap()
            .toSortedMap(compareByDescending { it })
            .printTen()


    val sizeThreshold = 1000

    fun trimToAverage(data: Map<String, List<String>> = this.data): Map<String, List<String>> {
        // order by appearance
        val sizeMap = flatKeys
                .groupBy { it }
                .alsoPrint { "Unique LOVs: ${it.size}" }
                .map { (label, list) -> label to list.size }
                .toMap()

        return data
                .mapValues { (_, labels) -> processLabels(labels, sizeMap) } // process labels
                .filterValues { it.isNotEmpty() } // if there are no more labels, filter out this trial
                .alsoPrint { "Final size: ${it.size}. Truncated ${data.size - it.size}" }
    }

    fun processLabels(labels: List<String>, sizeMap: Map<String, Int>) = labels
            .filter { sizeMap[it]!! > sizeThreshold } // filter out labels that are below the threshold
            .sortedByDescending { sizeMap[it] } // sort by appearance
            .take(average.toInt()) // take the average max

}