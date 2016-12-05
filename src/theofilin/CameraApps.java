/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package theofilin;

import ij.ImagePlus;
import ij.process.ImageProcessor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.Timer;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import org.apache.commons.io.IOUtils;

/**
 *
 * @author noxymon
 */
public class CameraApps {

    /**
     * @param args the command line arguments
     */
    public static BufferedImage img;
    static Timer timer;
    
    static ImagePlus impl, impl2;
    static ImageProcessor ip;
    
    public static void StartCamera(JLabel tampil, int width, int height) throws IOException {
        ActionListener actionlistener;
        actionlistener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                
                try {
                    Runtime rt = Runtime.getRuntime();
                    String command = "raspistill -n -t 1 -drc high -ISO 200 " + "-w " + width + " -h " + height + " -o -";
                    Process p = rt.exec(command);

                    InputStream is = p.getInputStream();
                    byte[] curr = IOUtils.toByteArray(is);
                    ImageIcon disp = new ImageIcon(curr);
                    tampil.setIcon(disp);
                } catch (IOException ex) {
                    Logger.getLogger(CameraApps.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        };
        timer = new Timer(100, actionlistener);
        timer.start();
    }
    
    public static void StopCamera(){
        timer.stop();
    }
    
    public static void mulailagi(){
        timer.start();
    }

    public static void GrabCamera(JLabel tampil,  int width, int height){
        
        try {
            Runtime rt = Runtime.getRuntime();
            String command = "raspistill -n -t 1 -drc high -ISO 200 " + "-w " + width + " -h " + height + " -o -";
            Process p = rt.exec(command);
            
            //tampilin di label dulu
            
            InputStream is = p.getInputStream();
            byte[] curr = IOUtils.toByteArray(is);
            ImageIcon disp = new ImageIcon(curr);
            tampil.setIcon(disp);
            
            //reset InputStream
            is = null;
            
            //tulis difile kemudian
            is = new ByteArrayInputStream(curr);
            img = ImageIO.read(is);
            
            impl = new ImagePlus(null, img);
            ip = impl.getProcessor();
        } catch (IOException ex) {
            Logger.getLogger(CameraApps.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void ResetCamera(JLabel tampil,  int width, int height) throws IOException {
        timer.stop();
        StartCamera(tampil, width, height);
    }

    public static void main(String[] args)  {
        // TODO code application logic here
    }
    
    public static void BurstNow(JLabel tampil, double r_gain, double b_gain, int width, int height, int num) throws IOException{      
        try {
            Runtime rt = Runtime.getRuntime();
            String command = "raspistill -n --raw -t 1 -sa 0 -co 0 -br 40 -sh 0 -awb off -awbg " + r_gain +","+ b_gain + " -ISO 200 -ss 45000 " + "-w " + width + " -h " + height + " -o -";
            Process p = rt.exec(command);

            InputStream is = p.getInputStream();
            byte[] curr = IOUtils.toByteArray(is);
            ImageIcon disp = new ImageIcon(curr);
            tampil.setIcon(disp);
            is = new ByteArrayInputStream(curr);
            img = ImageIO.read(is);
            ImageIO.write(img, "jpg", new File("/home/pi/" + num + ".jpg"));
        } catch (IOException ex) {
            Logger.getLogger(CameraApps.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
