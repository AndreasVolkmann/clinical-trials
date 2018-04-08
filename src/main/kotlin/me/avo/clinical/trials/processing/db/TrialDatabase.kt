package me.avo.clinical.trials.processing.db

import me.avo.clinical.trials.processing.Trial
import me.avo.clinical.trials.processing.removeNewlineTrimSpaces
import me.avo.clinical.trials.processing.trimQuotesAndSpace
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class TrialDatabase {

    private val port = 5432
    private val db = "aact"
    private val url = "jdbc:postgresql://aact-db.ctti-clinicaltrials.org:$port/$db"
    private val user = "aact"
    private val pass = "aact"
    private val driver = "org.postgresql.Driver"

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
        (BriefSummaries innerJoin Keywords
                leftJoin Studies
                leftJoin Conditions
                leftJoin Interventions
                leftJoin DesignGroups
                leftJoin DesignOutcomes
                leftJoin Sponsors
                )
            .slice(
                BriefSummaries.columns + Keywords.name +
                        Studies.officialTitle + Studies.briefTitle +
                        Conditions.name +
                        Interventions.name + Interventions.type + Interventions.description +
                        DesignGroups.title + DesignGroups.groupType + DesignGroups.description +
                        DesignOutcomes.measure + DesignOutcomes.timeframe + DesignOutcomes.description +
                        Sponsors.name
            )
            .select {
                if (keywords.isEmpty()) Keywords.name.isNotNull()
                else Keywords.name inList keywords
            }
            .groupBy { it[BriefSummaries.nctId] }
            .values
            .map {
                val firstRow = it.first()
                Trial(
                    id = firstRow[BriefSummaries.nctId],
                    title = firstRow[Studies.officialTitle] ?: firstRow[Studies.briefTitle],
                    summary = firstRow[BriefSummaries.description].trimQuotesAndSpace().removeNewlineTrimSpaces(),
                    keywords = it.map { it[Keywords.name] }.distinct(),
                    conditions = it.mapNotNull { it[Conditions.name] }.distinct(),
                    more = it.flatMap {
                        listOfNotNull(
                            it[Interventions.name],
                            it[Interventions.description],
                            it[Interventions.type],
                            it[DesignGroups.groupType],
                            it[DesignGroups.description],
                            it[DesignGroups.title],
                            it[DesignOutcomes.description],
                            it[DesignOutcomes.measure],
                            it[DesignOutcomes.timeframe],
                            it[Sponsors.name]
                        )
                    }.distinct().joinToString(" ", transform = String::clean)
                )
            }
    }

}

private fun Table.nctId() = varchar("nct_id", 255) references BriefSummaries.nctId

object Studies : Table("studies") {
    val nctId = nctId()
    val officialTitle = text("official_title").nullable()
    val briefTitle = text("brief_title")
}

object BriefSummaries : Table("brief_summaries") {
    //val id = integer("id")
    val nctId = varchar("nct_id", 255)
    val description = text("description")
}

object Keywords : Table("keywords") {
    val id = integer("id")
    val nctId = nctId()
    val name = varchar("name", 255)
}

object Conditions : Table("conditions") {
    val nctId = nctId()
    val name = varchar("name", 255).nullable()
}

object Interventions : Table("interventions") {
    val nctId = nctId()
    val type = varchar("intervention_type", 255).nullable()
    val name = varchar("name", 255).nullable()
    val description = text("description").nullable()
}

object DesignGroups : Table("design_groups") {
    val nctId = nctId()
    val groupType = varchar("group_type", 255).nullable()
    val title = varchar("title", 255).nullable()
    val description = varchar("description", 255).nullable()
}

object DesignOutcomes : Table("design_outcomes") {
    val nctId = nctId()
    val measure = text("measure").nullable()
    val timeframe = text("time_frame").nullable()
    val description = text("description").nullable()
}

object Sponsors : Table("sponsors") {
    val nctId = nctId()
    val name = varchar("name", 255)
}