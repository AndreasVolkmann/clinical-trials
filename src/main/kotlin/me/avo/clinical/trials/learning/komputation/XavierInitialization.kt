package me.avo.clinical.trials.learning.komputation

import com.komputation.initialization.*
import com.komputation.matrix.*

class XavierInitialization internal constructor(): InitializationStrategy {

    override fun initialize(indexRow: Int, indexColumn: Int, numberIncoming: Int): Float {
        //val numberOutgoing: Int = TODO()
        //return 2 / (numberIncoming.toFloat() + numberOutgoing)
        //return 1 / numberIncoming.toFloat()
        return FloatMath.sqrt(2f / numberIncoming)
    }

}

fun xavierInitialization() = XavierInitialization()