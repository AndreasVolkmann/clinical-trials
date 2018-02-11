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

    fun getTopKeywords(): List<String> = transaction {
        Keywords
            .slice(Keywords.name)
            .selectAll()
            .groupBy(Keywords.name)
            .orderBy(Count(Keywords.name), isAsc = false)
            .limit(100)
            .map { it[Keywords.name] }
    }

    fun getTrials(keywords: List<String>): List<Trial> = transaction {
        (BriefSummaries innerJoin Keywords)
            .slice(BriefSummaries.columns + Keywords.name)
            .select {
                if (keywords.isEmpty()) Keywords.name.isNotNull()
                else Keywords.name inList keywords
            }
            .limit(100) // todo remove
            .groupBy { it[BriefSummaries.nct_id] }
            .values
            .map {
                val firstRow = it.first()
                Trial(
                    id = firstRow[BriefSummaries.nct_id],
                    summary = firstRow[BriefSummaries.description],
                    keywords = it.map { it[Keywords.name] }
                )
            }
    }

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