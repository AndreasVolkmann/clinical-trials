package me.avo.clinical.trials

import junit.framework.Assert.assertEquals
import org.amshove.kluent.*
import org.junit.Assert.assertNotEquals
import org.junit.jupiter.api.Test

internal class Test {

    data class Data(val id: Int)

    @Test
    fun run() {

        infix fun <T> T.shouldEqual(theOther: T?): T = this.apply { assertEquals(theOther, this) }


        val first = Data(1)
        val second = Data(2)

        first shouldEqual first

        val actual: Data? = Data(4)

        val ac: Data? = null

        ac shouldEqual null

        actual.shouldEqual(Data(4))


    }

    @Test
    fun not() {

        //infix fun Any?.shouldNotEqual(theOther: Any?) = this.apply { assertNotEquals(theOther, this) }
        infix fun <T> T.shouldNotEqual(theOther: T?): T = this.apply { assertNotEquals(theOther, this) }

        ("" shouldNotEqual "").shouldBeBlank()





    }

}