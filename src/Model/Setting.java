/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;



/**
 *
 * @author dttr2
 */
public class Setting {

    static String filename = "data/setting.json";
    static public SettingData settingData;

    static {
        try {
            ReadData();
        } catch (FileNotFoundException ex) {
            System.out.println("no setting data file found!");
            Logger.getLogger(Setting.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    static void ReadData() throws FileNotFoundException {
        Gson gson = new Gson();
        JsonReader reader = new JsonReader(new FileReader(filename));
        settingData = gson.fromJson(reader, SettingData.class);
    }

    static public void WriteData() throws IOException {
        FileWriter writer = new FileWriter(filename);
        Gson gson = new Gson();
        writer.write(gson.toJson(settingData, SettingData.class));
        writer.flush();
    }
}
