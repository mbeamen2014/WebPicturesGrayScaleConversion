package webpictures;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.concurrent.Callable;

/**
 * CLASS DESCRIPTION: ImageDownload is the long running task
 * that is submitted to the CompletionService in main. It implements a Callable
 * and--in the abstract call() method--downloads each image from the site. It will also 
 * call the grayScaleConversion() conversion method and return the name of the current 
 * thread to the main.
 */
public class ImageDownload implements Callable<String> {

   String fileName = "";

   /*
    * Constructor
    * @param fileName - The fileName name of the image being downloaded.
    */
   public ImageDownload(String file) {
      this.fileName = file;
   }

   /*
    * The abstract call method is implemented to connect to the URL and to 
    * convert the image to a byte[] array. The array is thn written to the local
    * project folder. 
    * @return thread_name
    */
   @Override
   public String call() {

      try {
         
         Thread.sleep(100);
         
         System.out.println((char)27 +  "[34mDownloading file: " + fileName + " in " 
                             + Thread.currentThread().getName()  
                             + (char)27 + "[0m");
         
         String path = "http://elvis.rowan.edu/~mckeep82/ccpsp15/Astronomy/" + fileName;
         URL url = new URL(path);
         InputStream input_stream = url.openStream();
         OutputStream output_stream = new FileOutputStream(fileName);
         byte[] buffer = new byte[2048];//byte[] array for image file.
         int length = 0;

         //Read the byte[] array and write locally
         while ((length = input_stream.read(buffer)) != -1) {
            output_stream.write(buffer, 0, length);

         }

         input_stream.close();
         output_stream.close();

         } catch (Exception e) {
         System.out.println("Error:" + e.getMessage());
      }
      //Returns String containing file name that will be passed to the conversion task.
      return fileName;
   }
   
}
