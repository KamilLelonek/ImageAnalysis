package helpers

import models.Point
import scala.collection.mutable.MutableList
import javax.imageio.ImageIO
import java.io.File
import java.util.ArrayList

object AnalyzedImage {
    def apply(path: String) = 
        new AnalyzedImage(s"images/${path}.sift", ImageIO read(new File(s"images/${path}.png")))
}

class AnalyzedImage(val siftPath: String, val image: java.awt.Image, val points: ArrayList[Point] = new ArrayList)