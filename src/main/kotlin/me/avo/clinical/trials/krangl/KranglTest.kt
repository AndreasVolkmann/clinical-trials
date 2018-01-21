package me.avo.clinical.trials.krangl

import krangl.*
import org.apache.commons.csv.*
import java.io.*

fun main(args: Array<String>) {

    val df = DataFrame.fromCSV(
            File("trials.txt"),
            CSVFormat.DEFAULT.withFirstRecordAsHeader().withDelimiter('|'))

    df.glimpse()
    df.sortedBy { it["Summary"].length }.head(5).print(true)

    val labelSet = mutableSetOf<String>()

    val trans = df.transmute(
            "Label" to { it["Label"].map(labelSet::categorize) },
            "Summary" to { it["Summary"] })

    trans.glimpse()
    trans.head(5).print(true)


}


fun MutableSet<String>.categorize(item: String): Int {
    val initialIndex = indexOf(item)
    return if (initialIndex == -1) {
        add(item)
        indexOf(item)
    } else initialIndex
}