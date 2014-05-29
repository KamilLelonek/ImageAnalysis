import helpers.AnalyzedImage
import helpers.ImageProcessor
import methods.Neighbourhood
import methods.Ransac
import java.io.File

object Main extends App {
    val imageIndex = 7
    val firstImage  = AnalyzedImage(imageIndex.toString)
    val secondImage = AnalyzedImage((imageIndex + 1).toString)

    ImageProcessor.processImage(firstImage)
    ImageProcessor.processImage(secondImage)
    
    (new Neighbourhood).findNearestPointMethod(firstImage, secondImage)
    (new Ransac).process(firstImage, 0.1f, 1.f, 1000, 0.001f)
    
    GUI.show(firstImage, secondImage)
}
