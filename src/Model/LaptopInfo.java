/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author dttr2
 */
public class LaptopInfo {
    
    private HashMap header;
    private HashMap data;
    private String file;
    private String header_real;
    
    public HashMap getHeader() {
        return header;
    }
    
    public void setHeader(HashMap header) {
        this.header = header;
    }
    
    public HashMap getData() {
        return data;
    }
    
    public void setData(HashMap data) {
        this.data = data;
    }
    
    public String getFile() {
        return file;
    }
    
    public void setFile(String file) {
        this.file = file;
    }
    
    public LaptopInfo(String file, String idName) {
        header = new HashMap();
        data = new HashMap();
        this.file = file;
        
        File f = new File(file);
        if (f.exists()) {
            try {
                BufferedReader bufferedReader = new BufferedReader(new FileReader(f));
                header_real = bufferedReader.readLine();
                if (header != null) {
                    String[] h = header_real.split(",");
                    for (int i = 0; i < h.length; i++) {
                        String string = h[i];
                        this.header.put(string, i);
                    }
                    String currentLine = null;
                    int id = (int) this.header.get(idName);
                    String[] c;
                    while ((currentLine = bufferedReader.readLine()) != null) {
                        c = currentLine.split(",");
                        data.put(c[id], c);
                    }
                }
            } catch (FileNotFoundException ex) {
                Logger.getLogger(LaptopInfo.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(LaptopInfo.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public boolean saveToCsv(String p) {
        try {
            File f = new File(p);
            if (f.exists()) {
                if (f.delete()) {
                    f.createNewFile();
                }
            }
            BufferedWriter writer = new BufferedWriter(new FileWriter(f));
            String h = String.join(",", header_real);
            writer.write(h);
            writer.newLine();
            writer.flush();
            for (Object s : data.values()) {
                String[] ar = (String[]) s;
                h = String.join(",", ar);
                writer.write(h);
                writer.newLine();
                writer.flush();
            }
//            File f1 = new File(p);
//            if(f1.exists())f1.delete();
//            f.renameTo(f1);
            
        } catch (IOException ex) {            
            Logger.getLogger(LaptopInfo.class.getName()).log(Level.SEVERE, null, ex);
            Setting.GetSettingData().writeLog(this.getClass().toString() + " | " + ex.getMessage() + " | " + "path:" + p);
            return false;
        }
        return true;
    }
}
