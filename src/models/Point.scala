package models

import java.awt.geom.Point2D

class Point(x: Float, y: Float, val properties: Array[Int] = new Array[Int](128), var nearest: Point = null) extends Point2D.Float(x, y)