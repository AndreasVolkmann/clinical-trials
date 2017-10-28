package me.avo.clinical.trials.tensor

import java.io.FileInputStream
import java.nio.ByteBuffer
import java.util.*
import kotlin.experimental.and


class MnistReader(val labelFile: String, val imageFile: String) {

    private val labelIO = FileInputStream(labelFile)
    private val imageIO = FileInputStream(imageFile)

    private var labelSize: Int = 0
    private var imageSize: Int = 0
    private var imageX: Int = 0
    private var imageY: Int = 0

    @Throws(Exception::class)
    fun readInt(`is`: FileInputStream): Int {
        val int32Full = ByteArray(4)
        `is`.read(int32Full)
        val wrapped = ByteBuffer.wrap(int32Full)
        return wrapped.int
    }

    fun size(): Int = imageSize

    init {
        try {
            if (readInt(labelIO) != 2049) throw Exception("Label file header missing")
            if (readInt(imageIO) != 2051) throw Exception("Image file header missing")

            labelSize = readInt(labelIO)
            imageSize = readInt(imageIO)

            if (labelSize != imageSize) throw Exception("Labels and images don't match in number.")

            imageY = readInt(imageIO)
            imageX = readInt(imageIO)

            println("LSZ $labelSize ISZ $imageSize Y $imageY X $imageX")

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun readNextLabel(): Int {
        try {
            return labelIO.read()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return -1
    }

    @Throws(Exception::class)
    fun readNextImage(): ByteArray {
        val imageArray = ByteArray(imageX * imageY)
        Arrays.fill(imageArray, 0.toByte())
        imageIO.read(imageArray, 0, imageX * imageY)
        return imageArray
    }

    companion object {

        @JvmStatic
        fun main(argv: Array<String>) {
            val mr = MnistReader("mnist/t10k-labels.idx1-ubyte", "mnist/t10k-images.idx3-ubyte")

            for (i in 0..199) print(mr.readNextLabel())
            print(mr.readNextLabel())

            for (i in 0..199) try {
                mr.readNextImage()
            } catch (e: Exception) {
                e.printStackTrace()
                println("Crash at " + i)
            }

            try {
                val b = mr.readNextImage()
                for (j in b.indices) {
                    if (j % 28 == 0) println()
                    print("${b[j] and 0xFF.toByte()} ")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }
}