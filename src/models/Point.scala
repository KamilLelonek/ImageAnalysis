package models

class Point(val x: Float, val y: Float, val properties: Array[Int] = new Array[Int](128), var nearest: Point = null)