package theofilin;

/**
 *
 * @author noxymon
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.encog.ml.data.MLData;
import org.encog.ml.data.basic.BasicMLData;
import org.encog.neural.networks.BasicNetwork;


public class EncogML {

    /**
     * @param args the command line arguments
     */
    
    static BasicNetwork ML;
    static double[] maximum;
    static double[] minimum;
    static double hasil1;
    static double hasil2;
    static String EnginePath;
    
    public static void ReadConfig(String path) throws FileNotFoundException{
        File file = new File(path);
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        String line;
        
        int count=0;
        String[] pecah;
        maximum = new double[10];
        minimum = new double[10];
        
        try {
            while((line = br.readLine()) != null){
                if (count+1==1){
                    pecah = line.split(",");
                    for(int i=0;i<pecah.length;i++){
                        maximum[i] = Double.valueOf(pecah[i]);
                    }
                }else if(count+1 ==2){
                    pecah = line.split(",");
                    for(int i=0;i<pecah.length;i++){
                        minimum[i] = Double.valueOf(pecah[i]);
                    }
                }else if(count+1 ==5){
                    EnginePath = line;
                }
                
                count++;
            }
            br.close();
            fr.close();
        } catch (IOException ex) {
            Logger.getLogger(EncogML.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private static double Normalisasi(double data, int index){
        double hasil=0;
        String konten;
        DecimalFormat df = new DecimalFormat("#.######");
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        
        hasil = (((data-minimum[index]) / (maximum[index] - minimum[index]) * 2) - 1);
        konten = dateFormat.format(date) + "," + "Normalisasi " + index + "," + minimum[index] + "," + maximum[index] + "," + hasil;
        clsLog.write(konten);
        
        return hasil;
    }
    
    public static int Hitung(double[] data){
        double[] input = new double[10];
        
        DecimalFormat df = new DecimalFormat("#.######");
        for(int j =0;j<data.length;j++){
            input[j] = Normalisasi(data[j], j);
        }
        
        MLData masuk = new BasicMLData(10);
        for (int i = 0; i<masuk.size();i++){
            masuk.setData(i, input[i]);
        }
        MLData output = ML.compute(masuk);
        int hasil=0;
        hasil1 = output.getData(0);
        hasil2 = output.getData(1);
        
        if (Double.valueOf(df.format(hasil1)) <= -0.866025){
            hasil = 1;
        }else if(Double.valueOf(df.format(hasil1)) > -0.866025){
            if(Double.valueOf(df.format(hasil1)) <= 0){
                hasil = 3;
            }else if(Double.valueOf(df.format(hasil1)) > 0){
                hasil = 2;
            }
        }
        return hasil;
    }
    
    public static void main(String[] args) {
        // TODO code application logic here
    }
    
}
