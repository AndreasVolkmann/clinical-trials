package me.avo.clinical.trials.update

import org.amshove.kluent.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.io.*

internal class UpdateCheckerTest {

    @Test fun check() {
        val file = File("D:\\DL\\clinical-trials")
        file.`should exist`()
        file.`should be dir`()
        val latest = UpdateChecker(file).check()
        println(latest)
    }

}