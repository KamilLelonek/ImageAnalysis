ImageAnalysis
=============

#### Image Analysis algorithms using Affine and Random Sample Consensus methods

1. Download processing tool from http://www.robots.ox.ac.uk/~vgg/research/affine/det_eval_files/extract_features2.tar.gz

2. To generate siff file type in Linux bash `./extract_features_32bit.ln -haraff -sift -i IMAGE.png -DE`.  
As a result you should get `IMAGE.siff` file.

3. Do previous operation for two images that you want to compare.

4. Set name of your images to compare in `Main.scala` and run the program.
