/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import Model.KMeansModel;
import Model.LaptopInfo;
import Model.Mapping;
import Model.Setting;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import weka.core.Instance;

/**
 *
 * @author dttr2
 */
public class DAO {

    static double m_manhinh;
    static double m_ram;
    static double m_dungluong;
    static double m_trongluong;
    static double m_gia;

    static HashMap loaibonho;
    static HashMap hm_cpu;
    static HashMap hm_gpu;
    static HashMap hm_class;

    static {
        build();
    }

    static public void build() {
        m_manhinh = 0;
        m_ram = 0;
        m_dungluong = 0;
        m_trongluong = 0;
        m_gia = 0;

        loaibonho = (HashMap) Setting.GetValueMapping().get("loaibonho");
        hm_cpu = (HashMap) Setting.GetValueMapping().get("cpu");
        hm_gpu = (HashMap) Setting.GetValueMapping().get("gpu");
        hm_class = (HashMap) Setting.GetValueMapping().get("class");
        LaptopInfo laptopInfo = Setting.GetLaptopInfo();
        try {
            for (Object value : laptopInfo.getData().values()) {
                String[] val = (String[]) value;
                String v = val[(int) laptopInfo.getHeader().get("manhinh")];
                double t = Double.valueOf(v);
                if (t > m_manhinh) {
                    m_manhinh = t;
                }
                t = 0;
                v = val[(int) laptopInfo.getHeader().get("ram")];
                t = Double.valueOf(v);
                if (t > m_ram) {
                    m_ram = t;
                }
                t = 0;
                v = val[(int) laptopInfo.getHeader().get("dungluongbonho")];
                t = Double.valueOf(v);
                if (t > m_dungluong) {
                    m_dungluong = t;
                }
                t = 0;
                v = val[(int) laptopInfo.getHeader().get("trongluong")];
                t = Double.valueOf(v);
                if (t > m_trongluong) {
                    m_trongluong = t;
                }
                t = 0;
                v = val[(int) laptopInfo.getHeader().get("gia")];
                t = Double.valueOf(v);
                if (t > m_gia) {
                    m_gia = t;
                }
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    static public void insertNewRowToDataProduct(HashMap row) throws Exception {
        String id = (String) row.get("id");
        if (id.trim().length() == 0) {
            id = (new Date()).getTime() + "";
        }
        double manhinh, ram, dungluongbonho, trongluong, gia;
        manhinh = Double.valueOf(row.get("manhinh").toString()) / m_manhinh;

        ram = Double.valueOf(row.get("ram").toString()) / m_ram;
        dungluongbonho = Double.valueOf(row.get("dungluongbonho").toString()) / m_dungluong;
        trongluong = Double.valueOf(row.get("trongluong").toString()) / m_trongluong;
        gia = Double.valueOf(row.get("gia").toString()) / m_gia;

        if (manhinh > 1) {
            manhinh = 1;
        }
        if (ram > 1) {
            ram = 1;
        }
        if (dungluongbonho > 1) {
            dungluongbonho = 1;
        }
        if (trongluong > 1) {
            trongluong = 1;
        }
        if (gia > 1) {
            gia = 1;
        }
        String class_name = "";

        double cpu_val = 0, gpu_val = 0, loaibonho_val = 0;

        cpu_val = (double) hm_cpu.get(row.get("cpu"));
        gpu_val = (double) hm_gpu.get(row.get("gpu"));

        loaibonho_val = (double) loaibonho.get(row.get("loaibonho"));

        HashMap h = new HashMap();
        h.put("manhinh", manhinh);
        h.put("ram", ram);
        h.put("loaibonho", loaibonho_val);
        h.put("dungluongbonho", dungluongbonho);
        h.put("trongluong", trongluong);
        h.put("gia", gia);
        h.put("cpu", cpu_val);
        h.put("gpu", gpu_val);

        double cls = KMeansModel.classOfIns(h);

        Iterator it = hm_class.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry m = (Map.Entry) it.next();
            String key = m.getKey().toString();
            double val = (double) m.getValue();
            if (val == cls) {
                class_name = key;
                break;
            }
        }
        HashMap header = Setting.GetLaptopInStoreVector().getHeader();
        String[] vector = new String[header.size()];
        vector[(int) header.get("manhinh")] = "" + manhinh;
        vector[(int) header.get("ram")] = "" + ram;
        vector[(int) header.get("cpu")] = "" + cpu_val;
        vector[(int) header.get("gpu")] = "" + gpu_val;
        vector[(int) header.get("trongluong")] = "" + trongluong;
        vector[(int) header.get("loaibonho")] = "" + loaibonho_val;
        vector[(int) header.get("dungluongbonho")] = "" + dungluongbonho;
        vector[(int) header.get("gia")] = "" + gia;
        vector[(int) header.get("name")] = "" + row.get("name");
        vector[(int) header.get("id")] = id;
        vector[(int) header.get("class")] = class_name;
        Setting.GetLaptopInStoreVector().getData().put(id, vector);

        HashMap header1 = Setting.GetLaptopInfo().getHeader();
        String[] info = new String[header1.size()];
        info[(int) header1.get("manhinh")] = "" + row.get("manhinh");
        info[(int) header1.get("ram")] = "" + row.get("ram");
        info[(int) header1.get("cpu")] = "" + row.get("cpu");
        info[(int) header1.get("gpu")] = "" + row.get("gpu");
        info[(int) header1.get("trongluong")] = "" + row.get("trongluong");
        info[(int) header1.get("loaibonho")] = "" + row.get("loaibonho");
        info[(int) header1.get("dungluongbonho")] = "" + row.get("dungluongbonho");
        info[(int) header1.get("gia")] = "" + row.get("gia");
        info[(int) header1.get("name")] = "" + row.get("name");
        info[(int) header1.get("id")] = id;
        info[(int) header1.get("class")] = class_name;
        Setting.GetLaptopInfo().getData().put(id, info);
    }

    public static void updateRowToDataProduct(HashMap row) throws Exception {
        insertNewRowToDataProduct(row);
    }

    public static void removeRowFromDataProduct(String id) {
        Setting.GetLaptopInStoreVector().getData().remove(id);
        Setting.GetLaptopInfo().getData().remove(id);
    }
}
