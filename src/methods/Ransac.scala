package methods

import Neighbourhood._
import helpers.AnalyzedImage
import models.Point
import scala.collection.JavaConversions._
import scala.util.Random._
import models.Triangle
import java.util.ArrayList
import java.util.Collections
import helpers.AffineTransform

class Ransac {
    def process(image: AnalyzedImage, r: Float, R: Float, iterations: Int, diff: Float) = {
        val bestModel = execute(image, r, R, iterations)
        image.points.foreach(point => {
            val imagePoint = bestModel.transform(point, null)
            if(imagePoint.distanceSq(point.nearest) > diff) {
                point.nearest.nearest = null
                point.nearest = null
            }
        })
        removeAlone(image)
    }

    def execute(image: AnalyzedImage, r: Float, R: Float, iterations: Int) = {
        var minError = Double.MaxValue
        var bestModel: AffineTransform = null

        (0 until iterations) foreach { i =>
            val triangle = chooseTriangle(image, r, R)
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

    def chooseTriangle(image: AnalyzedImage, r: Float, R: Float): Triangle = {
        val copyPoints = new ArrayList(image points)
        
        val firstPoint = copyPoints(nextInt(copyPoints size))
        removeFarPoints(copyPoints, firstPoint, null, r, R)
        if (copyPoints.size == 0) return null

        val secondPoint = copyPoints(nextInt(copyPoints size))
        removeFarPoints(copyPoints, firstPoint, secondPoint, r, R)
        if (copyPoints.size == 0) return null

        val thirdPoint = copyPoints(nextInt(copyPoints size))
        new Triangle(firstPoint, secondPoint, thirdPoint)
    }

    def calculateError(affineTransform: AffineTransform, image: AnalyzedImage) =
        image.points.foldLeft(0.d)((error, point) =>
            error + affineTransform.transform(point, null).distanceSq(point.nearest))

    def passTest(x1: Float, y1: Float, x2: Float, y2: Float, r: Float, R: Float) =
        r * r < (x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2) &&
            (x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2) < R * R

    def removeFarPoints(points: ArrayList[Point], firstPoint: Point, secondPoint: Point, r: Float, R: Float) = {
        val iterator = points listIterator(points size)
        while(iterator hasPrevious) {
            val point = iterator.previous
            val matchesFirstPoint = passTest(firstPoint.x, firstPoint.y, point.x, point.y, r, R)
            val matchesFirstImage = passTest(firstPoint.nearest.x, firstPoint.nearest.y, point.nearest.x, point.nearest.y, r, R)
            val matchesSecondPoint = if (secondPoint == null) true else passTest(secondPoint.x, secondPoint.y, point.x, point.y, r, R)
            val matchesSecondImage = if (secondPoint == null) true else passTest(secondPoint.nearest.x, secondPoint.nearest.y, point.nearest.x, point.nearest.y, r, R)

            if (!matchesFirstPoint ||
                !matchesFirstImage ||
                !matchesSecondPoint ||
                !matchesSecondImage ||
                point == firstPoint ||
                point == secondPoint)
                iterator remove
        }
    }
}