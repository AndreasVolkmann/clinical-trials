package me.avo.clinical.trials.update

import org.jsoup.*
import org.jsoup.nodes.*
import org.jsoup.select.*
import java.io.*
import java.text.*
import java.util.*

class UpdateChecker(val currentFileDate: Date) {

    constructor(dir: File) : this(Date(dir.lastModified()))

    fun check() = Jsoup.connect(url).get()
            .firstElementByClass("file-archive")
            .firstElementByTag("table")
            .getElementsByTag("tr")[1]
            .getElementsByTag("td")
            .let(this::makePipeFile)
            .also(this::evaluate)

    fun evaluate(pipeFile: PipeFile) = if (pipeFile.date.after(currentFileDate)) {
        println("New files available!")
        println("The latest files are from ${normalDateFormat.format(pipeFile.date)}")
        println("Download them from: $url")
    } else {
        println("You already have the latest files")
    }

    private fun makePipeFile(elements: Elements) = PipeFile(
            name = elements[0].text(),
            date = elements[1].text().let(ctDateFormat::parse),
            sizeInMb = elements[2].text().split(" ").first().toInt()
    )

    private val url = "http://aact.ctti-clinicaltrials.org/pipe_files"

    private val ctDateFormat = SimpleDateFormat("MM/dd/yyyy")

    private val normalDateFormat = SimpleDateFormat("yyyy-MM-dd")

    private fun Element.firstElementByClass(name: String): Element = getElementsByClass(name).first()

    private fun Element.firstElementByTag(name: String): Element = getElementsByTag(name).first()

}