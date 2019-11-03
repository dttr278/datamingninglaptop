/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import Model.KMeansModel;
import Model.MySQLConnUtils;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import weka.core.Instance;
import weka.core.Instances;

/**
 *
 * @author dttr2
 */
public class DAO {

    public static void main(String[] args) throws Exception {
//        KMeansModel.formatData();
//        updateDataFromLaptops();
        Instances ins = KMeansModel.readFile("data/Test.csv");
        for(int i=0;i<ins.size();i++){
            insertDBFromFile(ins.get(i));
        }
    }

    static ResultSet getLaptopInfo(double id) throws SQLException, ClassNotFoundException {
        Connection connection = MySQLConnUtils.getMySQLConnection();
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("SELECT l.*,d.cuahang FROM laptops l,data d where l.id=" + id + " and l.id=d.laptopid");
        rs.next();
        return rs;
    }

    static void insertDB(Instance in) throws ClassNotFoundException {
        float inch = (float) in.value(4);
        String[] dophangiai = in.stringValue(5).split(" ");
        String cpu = in.stringValue(6);

        cpu = cpu.trim();

        int ram = Integer.valueOf(in.stringValue(7).replace("GB", ""));
        String o = in.stringValue(8);

        String[] ocung;
        if (o.indexOf(" + ") > -1) {
            ocung = in.stringValue(8).split(" + ");
        } else {
            ocung = new String[1];
            ocung[0] = o;
        }
        String keuOCung;
        int dungLuong = 0;
        if (ocung.length == 2) {
            keuOCung = "SSD+HDD";
            String[] t = ocung[0].trim().split(" ");
            if (t[0].indexOf("TB") > -1) {
                dungLuong += (int) (Float.valueOf(t[0].replace("TB", "")) * 1024);
            } else {
                dungLuong += Integer.valueOf(t[0].replace("GB", ""));
            }
            t = ocung[1].trim().split(" ");
            if (t[0].indexOf("TB") > -1) {
                dungLuong += (int) (Float.valueOf(t[0].replace("TB", "")) * 1024);
            } else {
                dungLuong += Integer.valueOf(t[0].replace("GB", ""));
            }

        } else {

            keuOCung = "";
            String[] te = ocung[0].split(" ");
            for (int i = 1; i < te.length; i++) {
                keuOCung += te[i] + " ";
            }
            keuOCung = keuOCung.trim();
            String[] t = ocung[0].trim().split(" ");
            if (t[0].indexOf("TB") > -1) {
                dungLuong += (int) (Float.valueOf(t[0].replace("TB", "")) * 1024);
            } else {
                dungLuong += Integer.valueOf(t[0].replace("GB", ""));
            }
        }

        String gpu = in.stringValue(9);

        float trongluong = Float.valueOf(in.stringValue(11).replace("kg", ""));
        float gia = (float) in.value(12);
        String sql = "INSERT INTO `laptops` (`name`, `inches`, `dophangiai`, `cpu`, `ram`, `loaibonho`, `dungluong`, `gpu`, `trongluong`, `gia`, `class`)"
                + " VALUES ('" + in.stringValue(2) + "','" + inch + "','" + dophangiai[dophangiai.length - 1] + " ','" + cpu + "','" + ram + "','" + keuOCung + "','" + dungLuong + "','" + gpu + "','" + trongluong + "','" + gia + "','" + in.stringValue(13) + "' );";

        Connection connection;
        try {
            connection = MySQLConnUtils.getMySQLConnection();
            Statement statement = connection.createStatement();

            statement.executeUpdate(sql);
        } catch (SQLException ex) {
            Logger.getLogger(DAO.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println(ex.getMessage());
        }

    }

    static void insertDBFromFile(Instance in) throws ClassNotFoundException {
        float inch = (float) in.value(4);
        String[] dophangiai = in.stringValue(5).split(" ");
        String cpu = in.stringValue(6);

        cpu = cpu.trim();

        int ram = Integer.valueOf(in.stringValue(7).replace("GB", ""));
        String o = in.stringValue(8);

        String[] ocung;
        if (o.indexOf(" + ") > -1) {
            ocung = in.stringValue(8).split(" + ");
        } else {
            ocung = new String[1];
            ocung[0] = o;
        }
        String keuOCung;
        int dungLuong = 0;
        if (ocung.length == 2) {
            keuOCung = "SSD+HDD";
            String[] t = ocung[0].trim().split(" ");
            if (t[0].indexOf("TB") > -1) {
                dungLuong += (int) (Float.valueOf(t[0].replace("TB", "")) * 1024);
            } else {
                dungLuong += Integer.valueOf(t[0].replace("GB", ""));
            }
            t = ocung[1].trim().split(" ");
            if (t[0].indexOf("TB") > -1) {
                dungLuong += (int) (Float.valueOf(t[0].replace("TB", "")) * 1024);
            } else {
                dungLuong += Integer.valueOf(t[0].replace("GB", ""));
            }

        } else {

            keuOCung = "";
            String[] te = ocung[0].split(" ");
            for (int i = 1; i < te.length; i++) {
                keuOCung += te[i] + " ";
            }
            keuOCung = keuOCung.trim();
            String[] t = ocung[0].trim().split(" ");
            if (t[0].indexOf("TB") > -1) {
                dungLuong += (int) (Float.valueOf(t[0].replace("TB", "")) * 1024);
            } else {
                dungLuong += Integer.valueOf(t[0].replace("GB", ""));
            }
        }

        String gpu = in.stringValue(9);

        float trongluong = Float.valueOf(in.stringValue(11).replace("kg", ""));
        float gia = (float) in.value(12);
//        String sql = "INSERT INTO `laptops` (`name`, `inches`, `dophangiai`, `cpu`, `ram`, `loaibonho`, `dungluong`, `gpu`, `trongluong`, `gia`, `class`)"
//                + " VALUES ('" + in.stringValue(2) + "','" + inch + "','" + dophangiai[dophangiai.length - 1] + " ','" + cpu + "','" + ram + "','" + keuOCung + "','" + dungLuong + "','" + gpu + "','" + trongluong + "','" + gia + "','" + in.stringValue(13) + "' );";

        HashMap row = new HashMap();
        row.put("name", in.stringValue(2));
        row.put("inch", inch);
        row.put("cpu", cpu);
        row.put("ram", ram);
        row.put("loaibonho", keuOCung);
        row.put("dungluongbonho", dungLuong);
        row.put("gpu", gpu);
        row.put("trongluong", trongluong);
        row.put("gia", gia);
        row.put("inStore", 1);

        try {
            DAO.insertNewRow(row);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(AddRow.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(AddRow.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    static void updateValueGPU() throws ClassNotFoundException {
        String sql = "SELECT MAX(benmark) FROM gpu";
        Connection connection;
        float max;
        try {
            connection = MySQLConnUtils.getMySQLConnection();
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            rs.next();
            max = rs.getInt(1);

            rs = statement.executeQuery("select * from gpu");
            Statement statement1 = connection.createStatement();
            while (rs.next()) {
                statement1.executeUpdate("UPDATE `gpu` SET `value` = '" + rs.getFloat("benmark") / max + "' WHERE (`idgpu` = '" + rs.getInt("idgpu") + "');");
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAO.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println(ex.getMessage());
        }
    }

    static ResultSet getMappingByName(String type, String val) throws SQLException, ClassNotFoundException {
        Connection connection = MySQLConnUtils.getMySQLConnection();
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("SELECT * FROM valueMapping where type='" + type + "' and name like '%" + val + "%'");
        return rs;
    }

    static ResultSet getMappingById(String type, String id) throws SQLException, ClassNotFoundException {
        Connection connection = MySQLConnUtils.getMySQLConnection();
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("SELECT * FROM valueMapping where type='" + type + "' and idvalueMapping = '" + id + "'");
        rs.next();
        return rs;
    }

    static ResultSet getMappingByVal(String type, String val) throws SQLException, ClassNotFoundException {
        Connection connection = MySQLConnUtils.getMySQLConnection();
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("SELECT * FROM valueMapping where type='" + type + "' and value = '" + val + "'");
        rs.next();
        return rs;
    }

    static float m_manhinh;
    static float m_ram;
    static float m_dungluong;
    static float m_trongluong;
    static float m_gia;
    static int page;

    static HashMap loaibonho = new HashMap();
    static HashMap hm_cpu = new HashMap();
    static HashMap hm_gpu = new HashMap();
    static public HashMap hm_class = new HashMap();
    static public HashMap hm_class_val = new HashMap();

    static {
        try {
            Connection connection;
            connection = MySQLConnUtils.getMySQLConnection();

            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT count(*) count, MAX(inches) inch,Max(ram) ram,Max(dungluong) dungluong,Max(trongluong) trongluong,Max(gia) gia FROM laptops");
            rs.next();
            m_manhinh = rs.getInt("inch");
            m_ram = rs.getInt("ram");
            m_dungluong = rs.getInt("dungluong");
            m_trongluong = rs.getInt("trongluong");
            m_gia = rs.getInt("gia");
            page = rs.getInt("count") / 1000 + 1;

            loaibonho = new HashMap();
            hm_cpu = new HashMap();
            hm_gpu = new HashMap();
            hm_class = new HashMap();
            hm_class_val = new HashMap();

            ResultSet rs2 = getMappingByName("loaibonho", "");
            while (rs2.next()) {
                loaibonho.put(rs2.getString("name").toLowerCase().trim(), rs2.getDouble("idvalueMapping"));
            }
            ResultSet rscpu = getMappingByName("cpu", "");
            while (rscpu.next()) {
                hm_cpu.put(rscpu.getString("name").toLowerCase().trim(), rscpu.getDouble("idvalueMapping"));
            }
            ResultSet rsgpu = getMappingByName("gpu", "");
            while (rsgpu.next()) {
                hm_gpu.put(rsgpu.getString("name").toLowerCase().trim(), rsgpu.getDouble("idvalueMapping"));
            }
            ResultSet rsclass = getMappingByName("class", "");
            while (rsclass.next()) {
                hm_class.put(rsclass.getString("name").toLowerCase().trim(), rsclass.getDouble("idvalueMapping"));
                hm_class_val.put(rsclass.getDouble("idvalueMapping"), rsclass.getString("name").toLowerCase().trim());
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    static public void insertNewRow(HashMap row) throws SQLException, ClassNotFoundException, Exception {
        double manhinh, ram, dungluongbonho, trongluong, gia;
        manhinh = Double.valueOf(row.get("inch").toString()) / m_manhinh;
        ram = Double.valueOf(row.get("ram").toString()) / m_ram;
        dungluongbonho = Double.valueOf(row.get("dungluongbonho").toString()) / m_dungluong;
        trongluong = Double.valueOf(row.get("trongluong").toString()) / m_trongluong;
        gia = Double.valueOf(row.get("gia").toString()) / m_gia;

//        System.out.println(classOfIns(h));
        String class_name, cpu_id, gpu_id, loaibonho_id;

//        class_name = getMappingByVal("class",row.get("class").toString()).getString("name");
        cpu_id = hm_cpu.get(row.get("cpu")).toString();
        gpu_id = hm_gpu.get(row.get("gpu")).toString();
        loaibonho_id = loaibonho.get(row.get("loaibonho")).toString();

        double cpu_val, gpu_val, loaibonho_val;
        cpu_val = getMappingById("cpu", hm_cpu.get(row.get("cpu")).toString()).getDouble("value");
        gpu_val = getMappingById("gpu", hm_gpu.get(row.get("gpu")).toString()).getDouble("value");
        loaibonho_val = getMappingById("loaibonho", loaibonho.get(row.get("loaibonho")).toString()).getDouble("value");

        HashMap h = new HashMap();
        h.put("manhinh", manhinh);
        h.put("ram", ram);
        h.put("loaibonho", loaibonho_val);
        h.put("dungluongbonho", dungluongbonho);
        h.put("trongluong", trongluong);
        h.put("gia", gia);
        h.put("cpu", cpu_val);
        h.put("gpu", gpu_val);
        int cls = KMeansModel.classOfIns(h);

        class_name = getMappingByVal("class", "" + cls).getString("name");

        String sqlLaptop = "INSERT INTO laptops (name,inches,dophangiai,cpu,ram,loaibonho,dungluong,gpu,trongluong,gia,class)\n"
                + "VALUES ('" + row.get("name") + "'," + row.get("inch") + ",'','" + row.get("cpu") + "'," + row.get("ram") + ",'" + row.get("loaibonho") + "'," + row.get("dungluongbonho") + ",'" + row.get("gpu") + "'," + row.get("trongluong") + "," + row.get("gia") + ",'" + class_name + "');";
        Connection connection = MySQLConnUtils.getMySQLConnection();
        Statement statement = connection.createStatement();
        statement.executeUpdate(sqlLaptop);
        ResultSet rsId = statement.executeQuery("SELECT LAST_INSERT_ID();");
        rsId.next();
        String sqlData = "INSERT INTO `data` (manhinh,ram,loaibonho,dungluongbonho,trongluong,gia,class,laptopid,gpu,cpu,cuahang)\n"
                + "	VALUES (" + manhinh + "," + ram + "," + loaibonho_id + "," + dungluongbonho + "," + trongluong + "," + gia + "," + hm_class.get(class_name.toLowerCase().trim()) + "," + rsId.getInt(1) + "," + gpu_id + "," + cpu_id + "," + row.get("inStore") + ");";
        statement.executeUpdate(sqlData);
    }

    public static void updateRow(HashMap row) throws SQLException, ClassNotFoundException, Exception {
        double id = Double.valueOf(row.get("laptopid").toString());

        double manhinh, ram, dungluongbonho, trongluong, gia;
        manhinh = Double.valueOf(row.get("inch").toString()) / m_manhinh;
        ram = Double.valueOf(row.get("ram").toString()) / m_ram;
        dungluongbonho = Double.valueOf(row.get("dungluongbonho").toString()) / m_dungluong;
        trongluong = Double.valueOf(row.get("trongluong").toString()) / m_trongluong;
        gia = Double.valueOf(row.get("gia").toString()) / m_gia;

//        System.out.println(classOfIns(h));
        String class_name, cpu_id, gpu_id, loaibonho_id;

//        class_name = getMappingByVal("class",row.get("class").toString()).getString("name");
        cpu_id = hm_cpu.get(row.get("cpu")).toString();
        gpu_id = hm_gpu.get(row.get("gpu")).toString();
        loaibonho_id = loaibonho.get(row.get("loaibonho")).toString();

        double cpu_val, gpu_val, loaibonho_val;
        cpu_val = getMappingById("cpu", hm_cpu.get(row.get("cpu")).toString()).getDouble("value");
        gpu_val = getMappingById("gpu", hm_gpu.get(row.get("gpu")).toString()).getDouble("value");
        loaibonho_val = getMappingById("loaibonho", loaibonho.get(row.get("loaibonho")).toString()).getDouble("value");

        HashMap h = new HashMap();
        h.put("manhinh", manhinh);
        h.put("ram", ram);
        h.put("loaibonho", loaibonho_val);
        h.put("dungluongbonho", dungluongbonho);
        h.put("trongluong", trongluong);
        h.put("gia", gia);
        h.put("cpu", cpu_val);
        h.put("gpu", gpu_val);
        int cls = KMeansModel.classOfIns(h);

        class_name = getMappingByVal("class", "" + cls).getString("name");

        String sqlLaptop = "UPDATE laptops\n"
                + "	SET gia=" + row.get("gia") + ",dungluong=" + row.get("dungluongbonho") + ",trongluong=" + row.get("trongluong") + ",loaibonho='" + row.get("loaibonho") + "',gpu='" + row.get("gpu") + "',ram=" + row.get("ram") + ",inches=" + row.get("inch") + ",cpu='" + row.get("cpu") + "',dophangiai='',class='" + class_name + "',name='" + row.get("name") + "'\n"
                + "	WHERE id=" + id + ";";
//        String sqlLaptop = "INSERT INTO laptops (name,inches,dophangiai,cpu,ram,loaibonho,dungluong,gpu,trongluong,gia,class)\n"
//                + "VALUES ('" + row.get("name") + "'," + row.get("inch") + ",'','" + row.get("cpu") + "'," + row.get("ram") + ",'" + row.get("loaibonho") + "'," + row.get("dungluongbonho") + ",'" + row.get("gpu") + "'," + row.get("trongluong") + "," + row.get("gia") + ",'" + class_name + "');";
        Connection connection = MySQLConnUtils.getMySQLConnection();
        Statement statement = connection.createStatement();
        statement.executeUpdate(sqlLaptop);
//        ResultSet rsId = statement.executeQuery("SELECT LAST_INSERT_ID();");
//        rsId.next();
        String sqlData = "UPDATE `data`\n"
                + "	SET ram=" + ram + ",manhinh=" + manhinh + ",dungluongbonho=" + dungluongbonho + ",gpu=" + gpu_id + ",loaibonho=" + loaibonho_id + ",trongluong=" + trongluong + ",class=" + hm_class.get(class_name.toLowerCase().trim()) + ",cpu=" + cpu_id + ",gia=" + gia + ",cuahang=" + row.get("inStore") + "\n"
                + "	WHERE laptopid=" + id + ";";
//        String sqlData = "INSERT INTO `data` (manhinh,ram,loaibonho,dungluongbonho,trongluong,gia,class,laptopid,gpu,cpu,cuahang)\n"
//                + "	VALUES (" + manhinh + "," + ram + "," + loaibonho_id + "," + dungluongbonho + "," + trongluong + "," + gia + "," + hm_class.get(class_name) + "," + rsId.getInt(1) + "," + gpu_id + "," + cpu_id + "," + row.get("inStore") + ");";
        statement.executeUpdate(sqlData);

    }

    public static void removeRow(double id) throws SQLException, ClassNotFoundException {
        Connection connection = MySQLConnUtils.getMySQLConnection();
        Statement statement = connection.createStatement();
        statement.addBatch("delete from data where laptopid = " + id);
        statement.addBatch("delete from laptops where id = " + id);

        statement.executeBatch();
    }

    static void updateDataFromLaptops() throws ClassNotFoundException {
        Connection connection;
        String sql;
        try {
            connection = MySQLConnUtils.getMySQLConnection();
            Statement statement = connection.createStatement();
            Statement statement1 = connection.createStatement();
            ResultSet rs;
            int j = 1;
            for (int i = 0; i < page; i++) {
                rs = statement.executeQuery("select * from laptops LIMIT 1000 OFFSET " + 1000 * i);
                while (rs.next()) {
                    System.out.println(rs.getString("class").toLowerCase().trim());
                    sql = "INSERT INTO `data` (`manhinh`, `cpu`, `ram`, `loaibonho`, `dungluongbonho`, `trongluong`, `gia`, `class`,`gpu`, `laptopid`) "
                            + "VALUES "
                            + "('" + rs.getFloat("inches") / m_manhinh + "',"
                            + " '" + hm_cpu.get(rs.getString("cpu").toLowerCase().trim()) + "',"
                            + " '" + rs.getFloat("ram") / m_ram + "',"
                            + " '" + loaibonho.get(rs.getString("loaibonho").toLowerCase().trim()) + "',"
                            + " '" + rs.getFloat("dungluong") / m_dungluong + "',"
                            + " '" + rs.getFloat("trongluong") / m_trongluong + "',"
                            + " '" + rs.getFloat("gia") / m_gia + "',"
                            + " '" + hm_class.get(rs.getString("class").toLowerCase().trim()) + "',"
                            + " '" + hm_gpu.get(rs.getString("gpu").toLowerCase().trim()) + "',"
                            + " '" + rs.getInt("id") + "');";
                    System.out.println(j++);
                    System.out.println(sql);
                    System.out.println(rs.getString("cpu"));
                    statement1.executeUpdate(sql);
                }
            }

        } catch (SQLException ex) {
            Logger.getLogger(DAO.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println(ex.getMessage());
        }
    }
}
