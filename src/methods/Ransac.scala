package methods

import scala.util.Random._
import Neighbourhood._
import helpers.AffineTransform
import helpers.AnalyzedImage
import models.Point
import models.Point
import models.Triangle
import java.util.ArrayList
import scala.collection.JavaConversions._

class Ransac {
    def process(image: AnalyzedImage, r: Float, R: Float, iterations: Int, diff: Float) = {
        val bestModel = execute(image, r, R, iterations)
        image.points.foreach(point => {
            val imagePoint = bestModel.transform(point, new Point(0, 0, null)).asInstanceOf[Point]
            if(imagePoint.featureDistance(point.nearest) > diff) {
                point.nearest.nearest = null
                point.nearest = null
            }
        })
        removeAlone(image points)
    }

    def execute(image: AnalyzedImage, r: Float, R: Float, iterations: Int) = {
        var minError = Double.MaxValue
        var bestModel: AffineTransform = null

        (0 until iterations) foreach { i =>
            val triangle = chooArrayListriangle(image, r, R)
            if (triangle != null) {
                val affineTransform = new AffineTransform(triangle)
                val error = calculateError(affineTransform, image)
                if (error < minError) {
                    bestModel = affineTransform
                    minError = error
                    println(minError)
                }
            }
        }
        
        bestModel
    }

    def chooArrayListriangle(image: AnalyzedImage, r: Float, R: Float): Triangle = {
        val copyPoints = new ArrayList(image.points)
        
        val firstPoint = copyPoints.get(nextInt(copyPoints size))
        removeFarPoints(copyPoints, firstPoint, null, r, R)
        if (copyPoints.size == 0) return null

        val secondPoint = copyPoints.get(nextInt(copyPoints size))
        removeFarPoints(copyPoints, firstPoint, secondPoint, r, R)
        if (copyPoints.size == 0) return null

        val thirdPoint = copyPoints.get(nextInt(copyPoints size))
        new Triangle(firstPoint, secondPoint, thirdPoint)
    }

    def calculateError(affineTransform: AffineTransform, image: AnalyzedImage) =
        image.points.foldLeft(0.d)((error, point) =>
            error + affineTransform.transform(point, new Point(0, 0, null)).asInstanceOf[Point].featureDistance(point.nearest))

    def passTest(x1: Float, y1: Float, x2: Float, y2: Float, r: Float, R: Float) =
        r * r < (x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2) &&
            (x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2) < R * R

    def removeFarPoints(points: ArrayList[Point], firstPoint: Point, secondPoint: Point, r: Float, R: Float) = {
        val iterator = points.listIterator
        while (iterator hasNext) {
            val point = iterator.next
            val matchesFirstPoint = passTest(firstPoint.x, firstPoint.y, point.x, point.y, r, R)
            val matchesFirstImage = passTest(firstPoint.nearest.x, firstPoint.nearest.y, point.nearest.x, point.nearest.y, r, R)
            val matchesSecondPoint = if (secondPoint == null) true else passTest(secondPoint.x, secondPoint.y, point.x, point.y, r, R)
            val matchesSecondImage = if (secondPoint == null) true else passTest(secondPoint.nearest.x, secondPoint.nearest.y, point.nearest.x, point.nearest.y, r, R)

            if (!matchesFirstPoint ||
                !matchesFirstImage ||
                !matchesSecondPoint ||
                !matchesSecondImage ||
                point.equals(firstPoint) ||
                point.equals(secondPoint))
                iterator remove
        }
    }
}