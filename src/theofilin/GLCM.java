package theofilin;

//=====================================================
//      Name:           GLCM_Texture
//      Project:         Gray Level Correlation Matrix Texture Analyzer
//      Version:         0.4
//
//      Author:           Julio E. Cabrera
//      Date:             06/10/05
//      Comment:       Calculates texture features based in Gray Level Correlation Matrices
//			   Changes since 0.1 version: The normalization constant (R in Haralick's paper, pixelcounter here)
//			   now takes in account the fact that for each pair of pixel you take in account there are two entries to the 
//			   grey level co-ocurrence matrix
//	 		   Changes were made also in the Correlation parameter. Now this parameter is calculated according to Walker's paper

//=====================================================


//===========imports===================================
import ij.process.*;
import java.awt.*;

//===========source====================================
public class GLCM {
    static int step = 1;
    static String selectedStep = "0 degrees";
    static boolean doIcalculateASM = true;
    static boolean doIcalculateContrast = true;
    static boolean doIcalculateCorrelation = true;
    static boolean doIcalculateIDM = true;
    static boolean doIcalculateEntropy = true;

    static double asm_all, contrast_all, correlation_all, idm_all, entropy_all, sum_all;

    public double getASM(){
        return asm_all;
    }
    
    public double getContrast(){
        return contrast_all;
    }
    
    public double getCorrelation(){
        return correlation_all;
    }
    
    public double getIDM(){
        return idm_all;
    }
    
    public double getEntropy(){
        return entropy_all;
    }
    
    public double getSum(){
        return sum_all;
    }
    
    public static void run(ImageProcessor ip) {
        // This part get al the pixel values into the pixel [ ] array via the Image Processor

        byte[] pixels = (byte[]) ip.getPixels();
        int width = ip.getWidth();
        Rectangle r = ip.getRoi();

        // The variable a holds the value of the pixel where the Image Processor is sitting its attention
        // The variable b holds the value of the pixel which is the neighbor to the  pixel where the Image Processor is sitting its attention

        int a;
        int b;
        double pixelCounter = 0;

        //====================================================================================================
        // This part computes the Gray Level Correlation Matrix based in the step selected by the user

        int offset, i;
        double[][] glcm = new double[257][257];

        if (selectedStep.equals("0 degrees")) {
            for (int y = r.y; y < (r.y + r.height); y++) {
                offset = y * width;
                for (int x = r.x; x < (r.x + r.width); x++) {
                    i = offset + x;
                    a = 0xff & pixels[i];
                    b = 0xff & (ip.getPixel(x + step, y));
                    glcm[a][b] += 1;
                    glcm[b][a] += 1;
                    pixelCounter += 2;
                }
            }
        }	


        if (selectedStep.equals("90 degrees")) {
            for (int y = r.y; y < (r.y + r.height); y++) {
                offset = y * width;
                for (int x = r.x; x < (r.x + r.width); x++) {
                    i = offset + x;
                    a = 0xff & pixels[i];
                    b = 0xff & (ip.getPixel(x, y - step));
                    glcm[a][b] += 1;
                    glcm[b][a] += 1;
                    pixelCounter += 2;
                }
            }
        }

        if (selectedStep.equals("180 degrees")) {
            for (int y = r.y; y < (r.y + r.height); y++) {
                offset = y * width;
                for (int x = r.x; x < (r.x + r.width); x++) {
                    i = offset + x;
                    a = 0xff & pixels[i];
                    b = 0xff & (ip.getPixel(x - step, y));
                    glcm[a][b] += 1;
                    glcm[b][a] += 1;
                    pixelCounter += 2;
                }
            }
        }

        if (selectedStep.equals("270 degrees")) {
            for (int y = r.y; y < (r.y + r.height); y++) {
                offset = y * width;
                for (int x = r.x; x < (r.x + r.width); x++) {
                    i = offset + x;
                    a = 0xff & pixels[i];
                    b = 0xff & (ip.getPixel(x, y + step));
                    glcm[a][b] += 1;
                    glcm[b][a] += 1;
                    pixelCounter += 2;
                }
            }
        }
        //=====================================================================================================

        // This part divides each member of the glcm matrix by the number of pixels. The number of pixels was stored in the pixelCounter variable
        // The number of pixels is used as a normalizing constant

        for (a = 0; a < 257; a++) {
            for (b = 0; b < 257; b++) {
                glcm[a][b] = (glcm[a][b]) / (pixelCounter);
            }
        }
        //=====================================================================================================
        // This part calculates the angular second moment; the value is stored in asm

        if (doIcalculateASM == true) {
            double asm = 0.0;
            for (a = 0; a < 257; a++) {
                for (b = 0; b < 257; b++) {
                    asm = asm + (glcm[a][b] * glcm[a][b]);
                }
            }
            asm_all = asm;
        }

        //=====================================================================================================
        // This part calculates the contrast; the value is stored in contrast

        if (doIcalculateContrast == true) {
            double contrast = 0.0;
            for (a = 0; a < 257; a++) {
                for (b = 0; b < 257; b++) {
                    contrast = contrast + (a - b) * (a - b) * (glcm[a][b]);
                }
            }
            contrast_all = contrast;
        }

        //=====================================================================================================
        //This part calculates the correlation; the value is stored in correlation
        // px []  and py [] are arrays to calculate the correlation
        // meanx and meany are variables  to calculate the correlation
        //  stdevx and stdevy are variables to calculate the correlation

        if (doIcalculateCorrelation == true) {

                //First step in the calculations will be to calculate px [] and py []
                double correlation = 0.0;
                double px = 0;
                double py = 0;
                double meanx = 0.0;
                double meany = 0.0;
                double stdevx = 0.0;
                double stdevy = 0.0;

                for (a = 0; a < 257; a++) {
                        for (b = 0; b < 257; b++) {
                                px = px + a * glcm[a][b];
                                py = py + b * glcm[a][b];

                        }
                }



                // Now calculate the standard deviations
                for (a = 0; a < 257; a++) {
                        for (b = 0; b < 257; b++) {
                                stdevx = stdevx + (a - px) * (a - px) * glcm[a][b];
                                stdevy = stdevy + (b - py) * (b - py) * glcm[a][b];
                        }
                }


                // Now finally calculate the correlation parameter

                for (a = 0; a < 257; a++) {
                        for (b = 0; b < 257; b++) {
                                correlation = correlation + ((a - px) * (b - py) * glcm[a][b] / (stdevx * stdevy));
                        }
                }
                correlation_all = correlation;




        }
        //===============================================================================================
        // This part calculates the inverse difference moment

        if (doIcalculateIDM == true) {
            double IDM = 0.0;
            for (a = 0; a < 257; a++) {
                for (b = 0; b < 257; b++) {
                    IDM = IDM + (glcm[a][b] / (1 + (a - b) * (a - b)));
                }
            }
            idm_all = IDM;
        }

        //===============================================================================================
        // This part calculates the entropy

        if (doIcalculateEntropy == true) {
            double entropy = 0.0;
            for (a = 0; a < 257; a++) {
                for (b = 0; b < 257; b++) {
                    if (glcm[a][b] == 0) {} else {
                        entropy = entropy - (glcm[a][b] * (Math.log(glcm[a][b])));
                    }
                }
            }
            entropy_all = entropy;
        }

        double suma = 0.0;
        for (a = 0; a < 257; a++) {
            for (b = 0; b < 257; b++) {
                suma = suma + glcm[a][b];
            }
        }
        sum_all = suma;
    }
}