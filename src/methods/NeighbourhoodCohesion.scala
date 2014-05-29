package methods

import models.Point
import models.Pair
import scala.collection.JavaConversions._
import helpers.AnalyzedImage
import Neighbourhood._
import NeighbourhoodCohesion._
import java.util.ArrayList
import java.util.Collections

object NeighbourhoodCohesion {
    def checkDistance(firstPoint: Point, secondPoint: Point) = {
        val diffX = firstPoint.x - secondPoint.x
        val diffY = firstPoint.y - secondPoint.y
        diffX * diffX + diffY * diffY
    }
}

class NeighbourhoodCohesion {
    implicit class Crossable[X](xs: ArrayList[X]) {
        def cross[Y](ys: ArrayList[Y]) = for { x <- xs; y <- ys } yield (x, y)
    }

    def analyze(firstImage: AnalyzedImage, secondImage: AnalyzedImage, numberOfNeighbours: Int, cohesionLevel: Float) = {
        firstImage.points foreach (point => {
            val distanceArray   = new ArrayList[Pair]
            val neighboursArray = new ArrayList[Pair]

            calculateDistance(point, firstImage, distanceArray)
            findNeighbours(point, distanceArray, numberOfNeighbours)
            calculateDistance(point.nearest, secondImage, neighboursArray)
            findNeighbours(point.nearest, neighboursArray, numberOfNeighbours)

            val cohesion = checkCohesion(distanceArray, neighboursArray)
            val cohesionPercentage = cohesion.toFloat / numberOfNeighbours
            if (cohesionPercentage < cohesionLevel) point.nearest = null
        })
        removeAlone(firstImage)
        removeAlone(secondImage)
        println(firstImage.points.size)
        firstImage.points foreach (println)
    }

    def checkCohesion(distanceArray: ArrayList[Pair], neighboursArray: ArrayList[Pair]) = {
        distanceArray.cross(neighboursArray).foldLeft(0)((result, element) =>
            element match {
                case (p1, p2) => result + (if (p1.point.nearest == p2.point) 1 else 0)
            })
    }

    def calculateDistance(point: Point, image: AnalyzedImage, distanceArray: ArrayList[Pair]) = {
        image.points foreach (p =>
            if (p != point) distanceArray add new Pair(p, checkDistance(point, p)))
    }

    def findNeighbours(point: Point, distanceArray: ArrayList[Pair], number: Int) = {
        Collections sort (distanceArray)
        val toRemove = Array
        (distanceArray.length - 1 to number by -1) foreach (index => distanceArray remove index)
    }
}