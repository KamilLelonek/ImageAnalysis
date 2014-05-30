package models

import java.awt.geom.Point2D
import Math.abs

object Point {
    def apply(x: Float, y: Float, properties: List[Int]) = new Point(x, y, properties)
}

class Point(x: Float, y: Float, val properties: List[Int], var nearest: Point = null)
        extends Point2D.Float(x, y) {
    def featureDistance(point: Point) =
        this.properties.zip(point.properties).map(p => abs(p._1 - p._2)).sum
}