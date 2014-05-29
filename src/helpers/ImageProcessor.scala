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
            val x = nextLine(0).toFloat / image.getXResolution
            val y = nextLine(1).toFloat / image.getYResolution
            val point = new Point(x, y)

            (5 until nextLine.length - 5) foreach { index =>
                point.properties(index - 5) = nextLine(index).toInt
            }

            image.points add point
        })
        scanner close
    }
}