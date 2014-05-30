package methods

import Neighbourhood._
import helpers.AnalyzedImage
import models.Pair
import models.Point
import scala.collection.JavaConversions._

class NeighbourhoodCohesion {
    implicit class Crossable[X](xs: List[X]) {
        def cross[Y](ys: List[Y]) = for { x <- xs; y <- ys } yield (x, y)
    }

    def analyze(firstImage: AnalyzedImage, secondImage: AnalyzedImage, numberOfNeighbours: Int, cohesionLevel: Float) = {
        firstImage.points foreach (point => {
            val distancesArrayFirst   = calculateDistance(point, firstImage)
            val distancesArraySecond  = calculateDistance(point.nearest, secondImage)
            val neighboursArrayFirst  = findNeighbours(point, distancesArrayFirst, numberOfNeighbours)
            val neighboursArraySecond = findNeighbours(point.nearest, distancesArraySecond, numberOfNeighbours)

            val cohesion = checkCohesion(distancesArrayFirst, distancesArraySecond)
            val cohesionPercentage = cohesion.toFloat / numberOfNeighbours
            if (cohesionPercentage < cohesionLevel) point.nearest = null
        })
        removeAlone(firstImage)
        removeAlone(secondImage)
        println(firstImage.points.size)
        firstImage.points foreach (println)
    }

    def checkCohesion(distanceArray: List[Pair], neighboursArray: List[Pair]) = {
        distanceArray.cross(neighboursArray).foldLeft(0)((result, element) =>
            result + (if (element._1.point.nearest equals element._2.point) 1 else 0))
    }

    def calculateDistance(point: Point, image: AnalyzedImage) =
        image.points.map(p => if (!p.equals(point)) Some(new Pair(p, point distanceSq p)) else None).toList.flatten

    def findNeighbours(point: Point, distanceArray: List[Pair], number: Int) = distanceArray.sorted take number
}