package theofilin;

import ij.ImagePlus;
import ij.process.ImageProcessor;
import java.io.IOException;
import javax.swing.JLabel;
import ij.process.ImageConverter;
import de.lmu.ifi.dbs.jfeaturelib.features.Tamura;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
/**
 *
 * @author noxymon
 */
public class clsColor {
    
    static double[] fitur;
    static double[] texture;
    
    public static void GetFiturAVG(ImageProcessor imp, JLabel ProgText, JLabel ProgAnim) throws IOException{
        int[] rgb = new int[3];
        float RG,RB;
        float red_jum=0,green_jum=0,blue_jum=0;
        float ired_jum = 0,igreen_jum = 0,iblue_jum = 0,yellow_jum = 0,magenta_jum = 0,RG_jum = 0,RB_jum = 0;       
        int count=0;
        float progress;
        
        ProgAnim.setVisible(true);
        ProgText.setVisible(true);
        
        ImagePlus im = new ImagePlus(null, imp);
        ImageConverter img = new ImageConverter(im);
        
        img.convertToGray8();
        
        GLCM glcm = new GLCM();
        glcm.run(im.getProcessor());
        
        Tamura tmr = new Tamura();    
        tmr.run(imp);
        
        for(int i =0;i < imp.getWidth();i++){
            for(int j = 0;j < imp.getHeight();j++){
                imp.getPixel(i, j, rgb);
                
                if (rgb[1] == 0){
                    RG = 0;
                }else{
                    RG = (float)rgb[0]/rgb[1];
                }
                
                if (rgb[2] == 0){
                    RB = 0;
                }else{
                    RB = (float)rgb[0]/rgb[2];
                }
                
                red_jum += rgb[0];
                green_jum += rgb[1];
                blue_jum += rgb[2];
                ired_jum += RGB2Index(rgb[0],rgb[1],rgb[2], "ired");
                igreen_jum += RGB2Index(rgb[0],rgb[1],rgb[2], "igreen");
                iblue_jum += RGB2Index(rgb[0],rgb[1],rgb[2], "iblue");
                yellow_jum += RGB2CMYK(rgb[0], rgb[1], rgb[2], "yellow");
                magenta_jum += RGB2CMYK(rgb[0], rgb[1], rgb[2], "magenta");
                RG_jum += RG;
                RB_jum += RB;
                
                count++;
                progress = (((float)count/(imp.getWidth()*imp.getHeight())*100));
                ProgText.setText(String.format("%.2f", progress) + " %");
            }
        }
        
        fitur = new double[10];
        texture = new double[8];
        
        fitur[0] = red_jum/count;
        fitur[1] = green_jum / count;
        fitur[2] = blue_jum / count;
        fitur[3] = ired_jum / count;
        fitur[4] = igreen_jum /count;
        fitur[5] = iblue_jum /count;
        fitur[6] = yellow_jum /count;
        fitur[7] = magenta_jum/count;
        fitur[8] = RG_jum /count;
        fitur[9] = RB_jum / count;
        
        texture[0] = tmr.getFeatures().get(0)[0];
        texture[1] = tmr.getFeatures().get(0)[1];
        texture[2] = tmr.getFeatures().get(0)[2];
        texture[3] = glcm.getASM();
        texture[4] = glcm.getContrast();
        texture[5] = glcm.getCorrelation();
        texture[6] = glcm.getEntropy();
        texture[7] = glcm.getIDM();
        
        String konten;
        DecimalFormat df = new DecimalFormat("#.######");
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        konten = dateFormat.format(date) + "," + "Fitur" + "," + fitur[0] + "," + fitur[1] + "," + fitur[2] + "," + fitur[3] + "," + fitur[4] + "," + fitur[5] + "," + fitur[6] + "," + fitur[7] + "," + fitur[8] + "," + fitur[9] + "," + texture[0] + "," + texture[1] + "," + texture[2] + "," + texture[3] + "," + texture[4] + "," + texture[5] + "," + texture[6] + "," + texture[7];
        clsLog.write(konten);
        
        ProgAnim.setVisible(false);
        ProgText.setText("Finish !");
    }
    
    public static float RGBValue(int r, int g, int b, String mode){
        float hasil=0;
        
        switch(mode){
            case "red":
                hasil = r/255f;
                break;
            case "green":
                hasil = g/255f;
                break;
            case "blue":
                hasil = b/255f;
                break;
        }
        return hasil;
    }
    
    public static  float RGB2Index(int r, int g, int b, String mode){
        float ir,ig,ib,hasil = 0;

        if ((r+g+b) == 0){
            ir = 0;ig=0;ib=0;
            
        }else{
            ir =(float)r/(r+g+b);
            ig =(float)g/(r+g+b);
            ib = (float)b/(r+g+b);
        }
        
        switch(mode){
            case "ired":
                hasil = ir;
                break;
            case "iblue":
                hasil = ib;
                break;
            case "igreen":
                hasil = ig;
                break;
        }
        return hasil;
    }
    
    public static  float RGB2CMYK(int r, int g, int b, String mode){
        float ri,gi,bi;
        float k, hasil = 0;
        
        ri = r/255f;
        gi = g/255f;
        bi = b/255f;
        
        k = 1.0f - Math.max(ri, Math.max(gi, bi));
        
        if ((1-k)==0){
            hasil= 0;
        }else{
            switch (mode){
                case "cyan":
                    hasil =  (1.0f - ri - k) / (1.0f - k);
                    break;
                case "magenta":
                    hasil = (1.0f - gi - k) / (1.0f - k);
                    break;
                case "yellow":
                    hasil = (1.0f - bi - k) / (1.0f - k);
                    break;
                case "black":
                    hasil = k;
                    break;
            }
        }
        return hasil;
    }
}
