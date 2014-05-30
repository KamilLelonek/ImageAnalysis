package helpers

import java.io.File
import java.util.Scanner
import models.Point
import java.lang.Float

object ImageProcessor {
    def processImage(image: AnalyzedImage) = {
        val scanner = new Scanner(new File(image siftPath))
        scanner nextLine
        val numberOfPoints = scanner.nextLine.toInt
        (0 until numberOfPoints).foreach(_ => {
            val nextLine = scanner.nextLine split " "
            val x = nextLine(0).toFloat / image.image.getWidth(null)
            val y = nextLine(1).toFloat / image.image.getHeight(null)
            val features = (5 until nextLine.length) map (nextLine(_).toInt) toList

            image.points add Point(x, y, features)
        })
        scanner close
    }
}