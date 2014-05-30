package methods

import helpers.AnalyzedImage
import models.Point
import scala.collection.JavaConversions._
import Neighbourhood._
import scala.collection.immutable.HashMap
import java.util.ArrayList

object Neighbourhood {
    def removeAlone(points: ArrayList[Point]) = {
        val iterator = points.listIterator
        while (iterator hasNext) {
            val point = iterator.next
            if (point != null || point.nearest != null)
                iterator remove
        }
    }
}

class Neighbourhood {
    def findNearestPointMethod(firstImagePoints: ArrayList[Point], secondImagePoints: ArrayList[Point]) = {
        firstImagePoints foreach (point => findNearestPoint(point, secondImagePoints))
        secondImagePoints foreach (point => findNearestPoint(point, firstImagePoints))
        nullifyAlonePoints(firstImagePoints)
        removeAlone(firstImagePoints)
        removeAlone(secondImagePoints)
    }

    def findNearestPoint(point: Point, otherImagePoints: ArrayList[Point]) =
        point.nearest = otherImagePoints minBy { _.featureDistance(point) }

    def nullifyAlonePoints(points: ArrayList[Point]) = {
        points foreach (point =>
            if (!point.nearest.nearest.equals(point)) point.nearest = null)
    }
}