/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import Main.DAO;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import weka.clusterers.ClusterEvaluation;
import weka.clusterers.SimpleKMeans;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.CSVLoader;
import weka.core.converters.CSVSaver;
import weka.experiment.InstanceQuery;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;

public class KMeansModel {

    public static PrintStream err = null;
    public static int maxIteration = 1000;
    public static Instances data;
    public static Instances data_t;
    public static Instances data_p;
    public static Instances data_t_p;
    public static SimpleKMeans model;
    public static ClusterEvaluation clsEval;
    
    public static String classes[];
    static {
        build();
    }

    public static void build() {
        try {
            data = readDataDB("v_data");
            data_t = readDataDB("v_data_c");

            Remove remove = new Remove();
            remove.setInputFormat(data);
            Instances data1 = Filter.useFilter(data, remove);

            data1.deleteAttributeAt(data1.attribute("laptopid").index());
            data1.deleteAttributeAt(data1.attribute("laptop").index());

            Remove remove1 = new Remove();
            remove1.setAttributeIndices("" + 7);
            remove1.setInputFormat(data1);
            Instances dataToBeClustered = Filter.useFilter(data1, remove1);
            model = buildKMeans(dataToBeClustered);

            System.out.println("************Begin Kmeanmodel********************");
            System.out.println(model);
            System.out.println("************End Kmeanmodel********************");
            data_p = dataToBeClustered;
            remove.setInputFormat(data_t);
            data_t_p = Filter.useFilter(data_t, remove);
            data_t_p.deleteAttributeAt(data_t_p.attribute("laptop").index());
            data_t_p.deleteAttributeAt(data_t_p.attribute("laptopid").index());
            data_t_p.deleteAttributeAt(data_t_p.attribute("class").index());

            
            clsEval = new ClusterEvaluation();
            clsEval.setClusterer(model);
            data1.setClassIndex(data1.attribute("class").index());
            clsEval.evaluateClusterer(data1);

            System.out.println(clsEval.clusterResultsToString());
            
            classes=getClassesOfCluster(data, data.attribute("class").index(), clsEval.getClassesToClusters());
        } catch (Exception ex) {
            Logger.getLogger(KMeansModel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static Instances readFile(String path) throws Exception {
        String filePath = path;
        File inFile = null;
        Instances data = null;
        inFile = new File(filePath);
        if (!inFile.exists()) {
            throw new Exception("Input file does not exist: " + filePath);
        }
        err = System.err;
        System.setErr(new PrintStream(new java.io.OutputStream() {
            public void write(int b) {
            }
        }));

        CSVLoader loader = new CSVLoader();
        loader.setSource(inFile);
        data = loader.getDataSet();
        return data;
    }

    public static Instances readDataDB(String table) throws Exception {
        Instances data = null;
        InstanceQuery query = new InstanceQuery();
        query.setCustomPropsFile(new File("data/DatabaseUtils.props"));
        query.setDatabaseURL(MySQLConnUtils.connectionURL);
        query.setUsername("root");
        query.setPassword("123456");
        query.setQuery("select * from " + table);
        data = query.retrieveInstances();
        return data;
    }

    public static SimpleKMeans buildKMeans(Instances data) throws Exception {
        // Create the KMeans object.
        SimpleKMeans kmeans = new SimpleKMeans();
        kmeans.setNumClusters(DAO.hm_class.size());
        kmeans.setMaxIterations(maxIteration);
        kmeans.setPreserveInstancesOrder(true);
        // Perform K-Means clustering.
        try {
            kmeans.buildClusterer(data);

        } catch (Exception ex) {
            err.println("Unable to buld Clusterer: " + ex.getMessage());
            ex.printStackTrace();
        }
        return kmeans;
    }

    public static void writeFile(Instances ins, String outputDir) {
        try {
            CSVSaver s = new CSVSaver();
            s.setFile(new File(outputDir));
            s.setInstances(ins);
            s.setFieldSeparator(",");
            s.writeBatch();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    static public String[] getClassesOfCluster(Instances data_t, int classIndex, int[] classes) {
        Map m = new HashMap();
        int i = 0, j = 0;
        int l = classes.length;
        String cl = null;
        while (i < Setting.settingData.getK()) {
            cl = data_t.get(j++).stringValue(classIndex);
            if (m.get(cl) == null) {
                m.put(cl, i++);
            }
        }
        Map m1 = new HashMap();
        m.forEach((t, u) -> {
            m1.put(u, t);
        });
        String[] rs = new String[l];
        for (int k = 0; k < l; k++) {
            rs[k] = (String) m1.get(classes[k]);
        }
        return rs;
    }

    //chuan hoa du lieu 
    public static void formatData() throws SQLException, ClassNotFoundException {
        Connection connection;
        connection = MySQLConnUtils.getMySQLConnection();
        Statement statement = connection.createStatement();
        String sql, sql1;
        int c;
        classes = getClassesOfCluster(data, data.attribute("class").index(), clsEval.getClassesToClusters());
        Instance ins;
        double as;
        double[] ass = clsEval.getClusterAssignments();
        for (int i = 0; i < ass.length; i++) {
            ins = data.get(i);
            as = ass[i];
            c = Integer.valueOf(String.valueOf(DAO.hm_class.get(classes[Integer.valueOf(String.valueOf(as).replace(".0", ""))].toLowerCase().trim())
            ).replace(".0", ""));
            sql = "UPDATE `data`\n"
                    + "	SET class=" + c + "\n"
                    + "	WHERE laptopid=" + ins.value(data.attribute("laptopid").index()) + ";";

            sql1 = "UPDATE `laptops`\n"
                    + "	SET class='" + classes[Integer.valueOf(String.valueOf(as).replace(".0", ""))] + "'\n"
                    + "	WHERE id=" + ins.value(data.attribute("laptopid").index()) + ";";

            System.out.println(sql);
            System.out.println(statement.executeUpdate(sql));
            System.out.println(sql1);
            System.out.println(statement.executeUpdate(sql1));

        }
    }

    public static int classOfIns(HashMap h) throws Exception{
        Instances ins=new Instances(data_p, 1, 1);
        Instance in=ins.firstInstance();
        in.setValue(ins.attribute("manhinh"), Float.valueOf(h.get("manhinh").toString()));
        in.setValue(ins.attribute("ram"), Float.valueOf(h.get("ram").toString()));
        in.setValue(ins.attribute("loaibonho"), Float.valueOf(h.get("loaibonho").toString()));
        in.setValue(ins.attribute("dungluongbonho"), Float.valueOf(h.get("dungluongbonho").toString()));
        in.setValue(ins.attribute("trongluong"), Float.valueOf(h.get("trongluong").toString()));
        in.setValue(ins.attribute("gia"), Float.valueOf(h.get("gia").toString()));
        in.setValue(ins.attribute("cpu"), Float.valueOf(h.get("cpu").toString()));
        in.setValue(ins.attribute("gpu"), Float.valueOf(h.get("gpu").toString()));
        return model.clusterInstance(in);
    }
    public static void main(String[] args) throws Exception {
//        HashMap h=new HashMap();
//        Instance in=data_p.lastInstance();
//        h.put("manhinh", in.value(data_p.attribute("manhinh")));
//        h.put("ram", in.value(data_p.attribute("ram")));
//        h.put("loaibonho", in.value(data_p.attribute("loaibonho")));
//        h.put("dungluongbonho", in.value(data_p.attribute("dungluongbonho")));
//        h.put("trongluong", in.value(data_p.attribute("trongluong")));
//        h.put("gia", in.value(data_p.attribute("gia")));
//        h.put("cpu", in.value(data_p.attribute("cpu")));
//        h.put("gpu", in.value(data_p.attribute("gpu")));
//        System.out.println(classOfIns(h));
    }
}
