package Model;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author dttr2
 */
public class SettingData {

    private String fileData;
    private String fileProduct;
    private String fileDataLaptopInStore;
    private String fileValueMapping;
    private String logFile;
    public int maxIteration = 1000;

    public int getMaxIteration() {
        return maxIteration;
    }

    public void setMaxIteration(int maxIteration) {
        this.maxIteration = maxIteration;
    }

    public String getLogFile() {
        return logFile;
    }

    public void setLogFile(String logFile) {
        this.logFile = logFile;
    }

    public String getFileData() {
        return fileData;
    }

    public void setFileData(String fileData) {
        this.fileData = fileData;
    }

    public String getFileProduct() {
        return fileProduct;
    }

    public void setFileProduct(String fileProduct) {
        this.fileProduct = fileProduct;
    }

    public String getFileDataLaptopInStore() {
        return fileDataLaptopInStore;
    }

    public void setFileDataLaptopInStore(String fileDataLaptopInStore) {
        this.fileDataLaptopInStore = fileDataLaptopInStore;
    }

    public String getFileValueMapping() {
        return fileValueMapping;
    }

    public void setFileValueMapping(String fileValueMapping) {
        this.fileValueMapping = fileValueMapping;
    }

    public void writeLog(String str) {

        try {
            FileWriter writer;
            String time = (new SimpleDateFormat("yyyy-MM-dd hh:mm:ss")).format(new Date());
            File f = new File(logFile);
            if (!f.exists()) {
                f.createNewFile();
            }
            writer = new FileWriter(f);
            BufferedWriter bufferedWriter=new  BufferedWriter(writer);
            bufferedWriter.newLine();
            bufferedWriter.append(time + " : " + str);
            bufferedWriter.flush();
        } catch (IOException ex) {
            Logger.getLogger(SettingData.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public SettingData() {

    }
}
