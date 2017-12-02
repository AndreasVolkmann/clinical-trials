package me.avo.clinical.trials.komputation

import com.komputation.cpu.workflow.*
import java.awt.*

fun makeAfterEach(test: CpuTester, results: Pair<Int, Float>, highScore: Float, update: (Pair<Int, Float>) -> Unit) = fun(i: Int, f: Float) {
    val score = test.run()
    if (score > results.second) {
        update(i to score)
    }
    if (score > highScore) {
        Toolkit.getDefaultToolkit().beep()
    }
    println("$i) $score - loss: $f")
}