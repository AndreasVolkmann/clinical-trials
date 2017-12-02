package me.avo.clinical.trials.komputation

import com.beust.jcommander.*

class Args(args: Array<String>) {

    @Parameter(names = ["-ed", "--embedding-dimension"], description = "Specify the embedding dimension for Glove")
    var embeddingDimension: Int = 50
        private set

    @Parameter(names = ["-i", "--iterations"], description = "The number of iterations for the model")
    var iterations: Int = 25
        private set

    @Parameter(names = ["-s", "--size"], description = "The size of the input data")
    var size: Int = 10_000
        private set

    @Parameter(names = ["-h", "--help"], help = true)
    var help = false
        private set

    init {
        println(args.toList())
        parse(args)
    }

    fun parse(args: Array<String>) = JCommander.newBuilder().addObject(this).build().let {
        it.parse(*args)
        if (help) {
            it.programName = "Clinical Trials"
            it.usage()
        }
    }

}