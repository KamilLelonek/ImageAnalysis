package helpers

import models.Point
import models.Point
import models.Triangle
import models.Triangle

object AffineTransform {
    def apply(t: Triangle) = 
        new AffineTransform(
            calculateA(t),
            calculateB(t),
            calculateC(t),
            calculateD(t),
            calculateE(t),
            calculateF(t)
            )

    private def calculateDenominator(t: Triangle) =
        (t.b.x - t.a.x) * t.c.y +
        (t.a.x - t.c.x) * t.b.y +
        (t.c.x - t.b.x) * t.a.y
        
    private def calculateA(t: Triangle) =
        ((t.b.nearest.x - t.a.nearest.x) * t.c.y +
         (t.a.nearest.x - t.c.nearest.x) * t.b.y +
         (t.c.nearest.x - t.b.nearest.x) * t.a.y) / calculateDenominator(t)
    
    private def calculateB(t: Triangle) =
        ((t.a.nearest.x - t.b.nearest.x) * t.c.x +
         (t.c.nearest.x - t.a.nearest.x) * t.b.x +
         (t.b.nearest.x - t.c.nearest.x) * t.a.x) / calculateDenominator(t)
        
    private def calculateC(t: Triangle) =
        ((t.a.nearest.x * t.b.x - t.b.nearest.x * t.a.x) * t.c.y +
         (t.c.nearest.x * t.a.x - t.a.nearest.x * t.c.x) * t.b.y +
         (t.b.nearest.x * t.c.x - t.c.nearest.x * t.b.x) * t.a.y) / calculateDenominator(t)
         
    private def calculateD(t: Triangle) =
        ((t.b.nearest.y - t.a.nearest.y) * t.c.y +
         (t.a.nearest.y - t.c.nearest.y) * t.b.y +
         (t.c.nearest.y - t.b.nearest.y) * t.a.y) / calculateDenominator(t)
         
    private def calculateE(t: Triangle) =
        ((t.a.nearest.y - t.b.nearest.y) * t.c.x +
         (t.c.nearest.y - t.a.nearest.y) * t.b.x +
         (t.b.nearest.y - t.c.nearest.y) * t.a.x) / calculateDenominator(t)
         
    private def calculateF(t: Triangle) =
        ((t.a.nearest.y * t.b.x - t.b.nearest.y * t.a.x) * t.c.y +
         (t.c.nearest.y * t.a.x - t.a.nearest.y * t.c.x) * t.b.y +
         (t.b.nearest.y * t.c.x - t.c.nearest.y * t.b.x) * t.a.y) / calculateDenominator(t) 
}

class AffineTransform(val a: Float, val b: Float, val c: Float, val d: Float, val e: Float, val f: Float) {
    def getMatrix =
        Array(
            Array(a, b, c),
            Array(d, e, f),
            Array(0, 0, 1)
        )

    def execute(point: Point) = {
        val x = b * point.y + a * point.x + c
        val y = e * point.y + d * point.x + f
        new Point(x, y)
    }
}