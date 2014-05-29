package methods

import helpers.AnalyzedImage
import models.Point
import scala.collection.JavaConversions._
import Neighbourhood._
import java.util._

object Neighbourhood {
    def removeAlone(image: AnalyzedImage) = {
        val iterator = image.points listIterator(image.points size)
        while(iterator hasPrevious) {
            val point = iterator.previous
            if(point.nearest == null) iterator remove
        }
    }
}

class Neighbourhood {
    def findNearestPointMethod(firstImage: AnalyzedImage, secondImage: AnalyzedImage) = {
        firstImage .points foreach(point => findNearestPoint(point, secondImage points))
        secondImage.points foreach(point => findNearestPoint(point, firstImage  points, true))
        nullifyAlonePoints(firstImage)
        removeAlone(firstImage)
        removeAlone(secondImage)
    }
    
    def findNearestPoint(point: Point, surroundings: ArrayList[Point], back: Boolean = false) = {
        val distances = new HashMap[Point, Float]()
        surroundings foreach(s => distances put(s, findDistance(s, point)))
        val minDistance = distances.values.min
        distances.keySet foreach(key =>
            if(minDistance == distances.get(key)) {
                if(back && key.nearest == point)
                    point.nearest = key
                else
                    point.nearest = key
            }
        )
    }
    
    def findDistance(firstPoint: Point, secondPoint: Point) = {
        val zippedPoints = firstPoint.properties zip secondPoint.properties
        zippedPoints.foldLeft(0.f)((result, element) => 
            result + (element match { case (p1, p2) => (p1 - p2) * (p1 - p2) }))
    }
    
    def nullifyAlonePoints(image: AnalyzedImage) = {
        image.points foreach(point =>
            if(point.nearest.nearest != point) point.nearest = null
        )
    }
}