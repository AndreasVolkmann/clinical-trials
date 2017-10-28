package me.avo.clinical.trials.komputation

import com.komputation.cpu.network.Network
import com.komputation.cpu.workflow.CpuTester

interface NetworkImplementation {

    fun build(processedData: ProcessedData): Pair<Network, CpuTester>

}