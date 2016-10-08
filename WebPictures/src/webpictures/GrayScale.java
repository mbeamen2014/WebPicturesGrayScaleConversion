package webpictures;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.Callable;
import javax.imageio.ImageIO;

/**
 *
 * GrayScale class that implements Callable and includes a
 * call() method that handles the GrayScale conversion after accepting the
 * String including the file name of the current file.
 */
public class GrayScale implements Callable<String> {

   String file = "";
   File localFile;
   BufferedImage image;

   /*
    * Constructor: Accepts the file name of the current image file
    * and converts to grayscale.
    * @param file - Name of the current file
    */ 
   public GrayScale(String file) throws IOException {
      this.file = file;
      localFile = new File(file);
   }

   /*
    * The abstract call() method takes the file name 
    * and converts the colors to gray scale and saves the file. 
    * 
    * 
    */
   @Override
   public String call() {
      System.out.println((char) 27 + "[36mConverting to grayscale: " + file + "in "
              + Thread.currentThread().getName()
              + (char) 27 + "[0m");
      try {

         //Source of GrayScale conversion code: http://www.tutorialspoint.com/java_dip/grayscale_conversion.htm
         image = ImageIO.read(localFile);
         int width = image.getWidth();
         int height = image.getHeight();

         for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
               Color c = new Color(image.getRGB(j, i));
               int red = (int) (c.getRed() * 0.299);
               int green = (int) (c.getGreen() * 0.587);
               int blue = (int) (c.getBlue() * 0.114);
               int total = red + green + blue;
               Color newColor = new Color(total, total, total);
               image.setRGB(j, i, newColor.getRGB());
            }
         }

         //Write converted image back to local directory.
         File ouptut = new File(file);
         ImageIO.write(image, "jpg", ouptut);
      } catch (Exception e) {
         e.printStackTrace();
      }
      return "";
   }
}
