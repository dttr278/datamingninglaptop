/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import weka.core.Instance;
import weka.core.Instances;

/**
 *
 * @author dttr2
 */
public class Setting {

    static private String filename = "data/setting.json";
    static private SettingData settingData;
    private static HashMap valueMapping;

    private static LaptopInfo laptopInfo;
    private static LaptopInfo laptopInStoreVector;

    public static void setLaptopInfo(LaptopInfo laptopInfo) {
        Setting.laptopInfo = laptopInfo;
    }

    public static void setLaptopInStoreVector(LaptopInfo laptopInStoreVector) {
        Setting.laptopInStoreVector = laptopInStoreVector;
    }

    static public LaptopInfo GetLaptopInfo() {
        if (laptopInfo == null) {
            ReadLaptopInfo();
        }
        return laptopInfo;
    }

    static public LaptopInfo GetLaptopInStoreVector() {
        if (laptopInfo == null) {
            ReadLaptopInStoreVector();
        }
        return laptopInStoreVector;
    }

    static public SettingData GetSettingData() {
        if (settingData == null) {
            try {
                ReadData();
            } catch (FileNotFoundException ex) {
                System.out.println("no setting data file found!");
                Logger.getLogger(Setting.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return settingData;
    }

    static public HashMap GetValueMapping() {
        if (valueMapping == null || valueMapping.size() == 0) {
            readValueMapping();
        }
        return valueMapping;
    }

    static {
        build();
    }

    static public void build() {
        try {
            ReadData();
        } catch (FileNotFoundException ex) {
            System.out.println("no setting data file found!");
            Logger.getLogger(Setting.class.getName()).log(Level.SEVERE, null, ex);
        }

        readValueMapping();
    }

    static void ReadData() throws FileNotFoundException {
        Gson gson = new Gson();
        JsonReader reader = new JsonReader(new FileReader(filename));
        settingData = gson.fromJson(reader, SettingData.class);
    }

    static public boolean WriteData() {
        FileWriter writer = null;
        try {
            File f = new File(filename);
            if (f.exists()) {
                f.delete();
            }
            f.createNewFile();
            writer = new FileWriter(f);
            Gson gson = new Gson();
            writer.write(gson.toJson(settingData, SettingData.class));
            writer.flush();

        } catch (IOException ex) {
            Logger.getLogger(Setting.class.getName()).log(Level.SEVERE, null, ex);
            GetSettingData().writeLog(Setting.class.getName() + " | " + ex.getMessage() + " | " + "path:" + filename);
            return false;
        } finally {
            try {
                writer.close();
            } catch (IOException ex) {
                Logger.getLogger(Setting.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return true;
    }

    static private void readValueMapping() {
        valueMapping = new HashMap();
        try {
            Instances ins = KMeansModel.readInstancesFromFile(settingData.getFileValueMapping());
            for (int i = 0; i < ins.size(); i++) {
                Instance in = ins.get(i);
                int indexType;
                indexType = ins.attribute("type").index();
                int indexName;
                indexName = ins.attribute("name").index();
                int indexValue;
                indexValue = ins.attribute("value").index();
                int indexRealValue;
                indexRealValue = ins.attribute("realvalue").index();

                if (valueMapping.get(in.stringValue(indexType)) == null) {
                    valueMapping.put(in.stringValue(indexType), new HashMap());
                }
                Mapping m = new Mapping(in.stringValue(indexName), in.value(indexValue), in.value(indexRealValue));
                ((HashMap) valueMapping.get(in.stringValue(indexType))).put(m.getName().toLowerCase().trim(), m.getValue());
            }
        } catch (Exception ex) {
            Logger.getLogger(Setting.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println(Setting.class.getName() + "---" + ex.getMessage());
            settingData.writeLog(Setting.class.getName() + "---" + ex.getMessage());
        }
    }

    static private void ReadLaptopInfo() {
        laptopInfo = new LaptopInfo(settingData.getFileProduct(), "id");
    }

    static private void ReadLaptopInStoreVector() {
        laptopInStoreVector = new LaptopInfo(settingData.getFileDataLaptopInStore(), "id");
    }
}
