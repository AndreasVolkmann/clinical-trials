package me.avo.clinical.trials.processing.db

import me.avo.clinical.trials.processing.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.*

class TrialDatabase {

    val port = 5432
    val db = "aact"
    val url = "jdbc:postgresql://aact-db.ctti-clinicaltrials.org:$port/$db"
    val user = "aact"
    val pass = "aact"
    val driver = "org.postgresql.Driver"

    fun connect() {
        Database.connect(url, driver, user, pass)
    }

    fun getTopKeywords(limit: Int = 10): List<String> = transaction {
        Keywords
            .slice(Keywords.name)
            .selectAll()
            .groupBy(Keywords.name)
            .orderBy(Count(Keywords.name), isAsc = false)
            .limit(limit)
            .map { it[Keywords.name] }
    }

    fun getTrials(keywords: List<String>): List<Trial> = transaction {
        (BriefSummaries innerJoin Keywords innerJoin Studies)
            .slice(BriefSummaries.columns + Keywords.name + Studies.officialTitle + Studies.briefTitle)
            .select {
                if (keywords.isEmpty()) Keywords.name.isNotNull()
                else Keywords.name inList keywords
            }
            .groupBy { it[BriefSummaries.nct_id] }
            .values
            .map {
                val firstRow = it.first()
                Trial(
                    id = firstRow[BriefSummaries.nct_id],
                    title = firstRow[Studies.officialTitle] ?: firstRow[Studies.briefTitle],
                    summary = firstRow[BriefSummaries.description].trimQuotesAndSpace().removeNewlineTrimSpaces(),
                    keywords = it.map { it[Keywords.name] }
                )
            }
    }

}

object Studies : Table("studies") {
    val nct_id = varchar("nct_id", 255) references BriefSummaries.nct_id
    val officialTitle = text("official_title").nullable()
    val briefTitle = text("brief_title")
}

object BriefSummaries : Table("brief_summaries") {
    val id = integer("id")
    val nct_id = varchar("nct_id", 255)
    val description = text("description")
}

object Keywords : Table("keywords") {
    val id = integer("id")
    val nct_id = varchar("nct_id", 255) references BriefSummaries.nct_id
    val name = varchar("name", 255)
}