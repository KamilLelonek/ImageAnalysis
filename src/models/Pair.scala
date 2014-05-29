package models

class Pair(val point: Point, val distance: Float) extends Ordered[Pair] {
    def compare(that: Pair) = this.distance.compareTo(that.distance)
}