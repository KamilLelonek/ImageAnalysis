import helpers.AnalyzedImage
import scala.collection.JavaConversions._
import java.awt.Graphics
import javax.swing.JPanel
import java.awt.Graphics2D
import javax.swing.JFrame
import javax.swing.SwingUtilities
import java.awt.Color
import java.awt.RenderingHints
import java.awt.BasicStroke
import scala.util.Random._

object GUI {
    class Frame(firstImage: AnalyzedImage, secondImage: AnalyzedImage) extends JFrame {
        val panel = new Panel(firstImage, secondImage)
        add(panel)
        setSize(1000, 600)
        setLocationRelativeTo(null)
        setTitle("Image comparison")
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
    }

    class Panel(firstImage: AnalyzedImage, secondImage: AnalyzedImage) extends JPanel {
        override def paintComponent(g: Graphics) = {
            super.paintComponent(g)

            val g2d = g.asInstanceOf[Graphics2D]
            val w: Float = getSize() width
            val h: Float = getSize() height

            val wCoeffIm1: Float = w / (2 * firstImage.image.getWidth(null))
            val hCoeffIm1: Float = h / firstImage.image.getHeight(null)
            val resizeIm1: Float = if (wCoeffIm1 < hCoeffIm1) wCoeffIm1 else hCoeffIm1

            val wCoeffIm2: Float = w / (2 * secondImage.image.getWidth(null))
            val hCoeffIm2: Float = h / secondImage.image.getHeight(null)
            val resizeIm2: Float = if (wCoeffIm2 < hCoeffIm2) wCoeffIm2 else hCoeffIm2

            g2d.drawImage(
                firstImage.image,
                0,
                0,
                (resizeIm1 * firstImage.image.getWidth(null)).toInt,
                (resizeIm1 * firstImage.image.getHeight(null)).toInt,
                Color.WHITE,
                null)

            g2d.drawImage(
                secondImage.image,
                (w / 2).toInt,
                0,
                (resizeIm2 * secondImage.image.getWidth(null)).toInt,
                (resizeIm2 * secondImage.image.getHeight(null)).toInt,
                Color.WHITE,
                null)

            g2d.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON)
            g2d.setStroke(new BasicStroke(2))

            firstImage.points foreach { point =>
                g2d.setColor(
                    new Color(
                        nextInt(255),
                        nextInt(255),
                        nextInt(255),
                        180))

                g2d.drawLine(
                    (point.x * resizeIm1 * firstImage.image.getWidth(null)).toInt,
                    (point.y * resizeIm1 * firstImage.image.getHeight(null)).toInt,
                    (w / 2 + point.nearest.x * resizeIm2 * secondImage.image.getWidth(null)).toInt,
                    (point.nearest.y * resizeIm2 * secondImage.image.getHeight(null)).toInt)
            }
        }
    }

    def show(firstImage: AnalyzedImage, secondImage: AnalyzedImage) = {
        SwingUtilities.invokeAndWait(new Runnable() {
            def run = {
                new Frame(firstImage, secondImage) setVisible (true)
            }
        })
    }
}