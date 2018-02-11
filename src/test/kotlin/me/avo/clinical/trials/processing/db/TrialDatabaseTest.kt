package me.avo.clinical.trials.processing.db

import org.junit.jupiter.api.*

internal class TrialDatabaseTest {

    @Test
    fun `connect and get top`() {
        with(TrialDatabase()) {
            connect()
            val keywords = getTopKeywords().take(5).onEach(::println)
            getTrials(keywords).forEach(::println)

        }
    }

}