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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.encog.mathutil.Equilateral;
import org.encog.ml.data.MLData;
import org.encog.ml.data.basic.BasicMLData;
import org.encog.neural.networks.BasicNetwork;


public class EncogTekstur {

    /**
     * @param args the command line arguments
     */
    
    static BasicNetwork ML;
    static double[] maximum_teksture;
    static double[] minimum_teksture;
    static String TextureEnginePath;
    
    static double hasil1;
    static double hasil2;
    static double hasil3;
    static double hasil4;
    static double hasil5;
    static double hasil6;
    static double hasil7;
    
    public static void ReadConfig(String path) throws FileNotFoundException{
        File file = new File(path);
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        String line;
        
        int count=0;
        String[] pecah;
        maximum_teksture = new double[8];
        minimum_teksture = new double[8];
        
        try {
            while((line = br.readLine()) != null){
                if(count+1 ==3){
                    pecah = line.split(",");
                    for(int i=0;i<pecah.length;i++){
                        maximum_teksture[i] = Double.valueOf(pecah[i]);
                    }
                }else if(count+1 ==4){
                    pecah = line.split(",");
                    for(int i=0;i<pecah.length;i++){
                        minimum_teksture[i] = Double.valueOf(pecah[i]);
                    }
                }else if(count+1==6){
                    TextureEnginePath = line;
                }
                count++;
            }
            br.close();
            fr.close();
        } catch (IOException ex) {
            Logger.getLogger(EncogTekstur.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private static double Normalisasi(double data, int index){
        double hasil=0;
        String konten;
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        
        hasil = (((data-minimum_teksture[index]) / (maximum_teksture[index] - minimum_teksture[index]) * 2) - 1);        
        konten = dateFormat.format(date) + "," + "Normalisasi tekstur " + index + "," + minimum_teksture[index] + "," + maximum_teksture[index] + "," + hasil;
        clsLog.write(konten);
        return hasil;
    }
    
    public static String Hitung(double[] data){
        double[] input = new double[8];
        Equilateral qq = new Equilateral(8, 1, -1);
        
        for(int j =0;j<data.length;j++){
            input[j] = Normalisasi(data[j], j);
        }
        
        MLData masuk = new BasicMLData(8);
        for (int i = 0; i<masuk.size();i++){
            masuk.setData(i, input[i]);
        }
        MLData output = ML.compute(masuk);
        String hasil=null;
        
        int grade = 0;
        grade = qq.decode(output.getData());
        
        switch (grade){
            case 0:
                hasil = "BBL";
                break;
            case 1:
                hasil = "BOP";
                break;
            case 2:
                hasil = "BOPF";
                break;
            case 3:
                hasil = "BTL";
                break;
            case 4:
                hasil = "BT";
                break;
            case 5:
                hasil = "BP";
                break;
            case 6:
                hasil = "Dust";
                break;
            case 7:
                hasil = "PF";
                break;
            default:
                hasil = "unidentified";
                break;
        }
        
        return hasil;
    }
    
    public static void main(String[] args) {
        // TODO code application logic here
    }
    
}