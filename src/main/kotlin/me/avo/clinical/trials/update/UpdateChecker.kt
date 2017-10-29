package me.avo.clinical.trials.update

import me.avo.clinical.trials.alsoPrint
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import java.text.SimpleDateFormat

class UpdateChecker {

    private val url = "http://aact.ctti-clinicaltrials.org/pipe_files"

    fun check() = Jsoup.connect(url).get()
            .firstElementByClass("file-archive")
            .firstElementByTag("table")
            .getElementsByTag("tr")[1]
            .getElementsByTag("td")
            .let(this::makePipeFile)
            .alsoPrint { "The latest files are from ${normalDateFormat.format(it.date)}" }

    private fun makePipeFile(elements: Elements) = PipeFile(
            name = elements[0].text(),
            date = elements[1].text().let(ctDateFormat::parse),
            sizeInMb = elements[2].text().split(" ").first().toInt()
    )

    private val ctDateFormat = SimpleDateFormat("mm/dd/yyyy")

    private val normalDateFormat = SimpleDateFormat("yyyy-mm-dd")

    fun Element.firstElementByClass(name: String): Element = getElementsByClass(name).first()

    fun Element.firstElementByTag(name: String): Element = getElementsByTag(name).first()

}