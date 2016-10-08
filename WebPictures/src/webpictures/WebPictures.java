package webpictures;

import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
/**
 *
 * CLASS DESCRIPTION: This class passes an ExecutorService
 * to a CompletionService, which will handle the asynchronous IO. From
 * docs.oracle.com: Producers submit tasks for execution. Consumers take
 * completed tasks and process their results in the order they complete. After
 * the Callable tasks are submitted to the CompletionService, Futures are used
 * to get the return value, which is then supported to a second CompletionService.
 */
public class WebPictures {

   /**
    * @param args the command line arguments
    */
   public static void main(String[] args) {

      /*Array of file names. I attempted to scan the directory and scrape the 
       * file names using a number of methods but I kept running into problems. 
       * This array is used only to store the file names.
       * Each String is passed to the ImageDownload task, which then opens the stream to the URL
       * and downloads. 
       */
     
      String[] fileList = {"EclipseSvalbard_Santikunaporn_960.jpg", "GS_20150401_SolarHalo_8814_DayNight.jpg",
         "HokusaiOblique_2015h800c.jpg", "M97_M108_LRGB_60p_APF_ckaltseis2015_1024.jpg",
         "Messier46DenisPRIOU1024.jpg", "Mooooonwalk_rjn_960.jpg", "MysticMountain_HubbleForteza_960.jpg",
         "N2903JewelofLeo_hallas_c1024.jpg", "Ring0644_hubble_960.jpg", "TLE2015_1024x821olsen.jpg",
         "TLEGoldenGate_rba_d.jpg", "TethysRingShadow_cassini_1080.jpg", "VirgoCentral_Subaru_960.jpg",
         "VolcanoWay_montufar_960.jpg", "barnard344_vanderHoeven_960.jpg", "hs-2015-13-a-web_voorwerpjes600h.jpg",
         "ngc3293_eso_960.jpg", "rNGC-4725-HaL(AOX)RGBpugh1024.jpg", "snowtrees_bonfadini_960.jpg",
         "tafreshi_MG_3456Pc2s.jpg"};
      
      //An ExecutorService and Completion Service for the threads that will handle the download task.
      ExecutorService exec = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() + 1);
      CompletionService<String> completion = new ExecutorCompletionService<>(exec);
      
      //An ExecutorService and Completion Service for the threads that will handle the conversion task.
      ExecutorService exec2 = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() + 1);
      CompletionService<String> completion2 = new ExecutorCompletionService<>(exec2);
     
      
      try {
         for (String file : fileList) {
            //Submit Callable ImageDownload task to CompletionService for each file in array.
            completion.submit(new ImageDownload(file));
         }
         for (int i = 0; i < fileList.length; i++) {
            //Retrieves and removes the Future representing the next completed task, waiting if none are yet present.
            Future<String> future = completion.take();
            //Submits the string returned by the first task to the second grayscale conversion task.
            completion2.submit(new GrayScale(future.get()));
            
         }
      } catch (Exception e) {
         e.printStackTrace();
      }
      //Shut down ExecutorServices. 
      exec.shutdown();
      exec2.shutdown();
     
   }
}
