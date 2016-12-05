/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 *
 * @author noxymon */

package theofilin;

import com.opencsv.CSVWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class clsLog {
    public static void write(String konten){
        CSVWriter writer;
        try {
            writer = new CSVWriter(new FileWriter(Utama.path + "/report.log", true), ',', CSVWriter.NO_QUOTE_CHARACTER);
            writer.writeNext(konten.split(","));
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(clsColor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
