package me.avo.clinical.trials.update

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class UpdateCheckerTest {

    @Test fun check() {
        val latest = UpdateChecker().check()
        println(latest)
    }

}