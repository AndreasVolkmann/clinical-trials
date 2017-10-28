package me.avo.clinical.trials.komputation

import com.beust.jcommander.JCommander
import com.beust.jcommander.Parameter

object Args {

    @Parameter(names = arrayOf("-ed", "--embedding-dimension"), description = "Specify the embedding dimension for Glove")
    var embeddingDimension: Int = 50
        private set

    @Parameter(names = arrayOf("-h", "--help"), help = true)
    var help = false
        private set


    fun parse(args: Array<String>) = JCommander.newBuilder().addObject(this).build().let {
        it.parse(*args)
        if (help) {
            it.programName = "Clinical Trials"
            it.usage()
        }
    }

}