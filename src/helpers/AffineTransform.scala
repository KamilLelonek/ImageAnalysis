package helpers

import models.Triangle

class AffineTransform(t: Triangle) extends java.awt.geom.AffineTransform(t.a.x, t.a.y, t.b.y, t.b.y, t.c.x, t.c.y)