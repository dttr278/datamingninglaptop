/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
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
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;

public class KMeansModel {

    public static PrintStream err = null;
    public static int maxIteration = Setting.GetSettingData().getMaxIteration();
    public static Instances data;
    public static Instances data_p;
    public static SimpleKMeans model;
    public static ClusterEvaluation clsEval;
    
    public static int k;
    
    public static String classes[];
    static {
        build();
    }

    public static void build() {
        try {
            k=((HashMap)Setting.GetValueMapping().get("class")).size();
            data = readInstancesFromFile(Setting.GetSettingData().getFileData());

            Remove remove = new Remove();
            remove.setInputFormat(data);
            Instances data1 = Filter.useFilter(data, remove);

            data1.deleteAttributeAt(data1.attribute("id").index());
            data1.deleteAttributeAt(data1.attribute("name").index());

            Remove remove1 = new Remove();
            remove1.setAttributeIndices((data1.attribute("class").index()+1)+"");
            remove1.setInputFormat(data1);
            Instances dataToBeClustered = Filter.useFilter(data1, remove1);
            model = buildKMeans(dataToBeClustered);

            System.out.println("************Begin Kmeanmodel********************");
            System.out.println(model);
            System.out.println("************End Kmeanmodel********************");
            data_p = dataToBeClustered;
            


            
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



    public static Instances readInstancesFromFile(String path) throws Exception {
        String filePath = path;
        File inFile = null;
        Instances data = null;
        inFile = new File(filePath);
        if (!inFile.exists()) {
            throw new Exception("Input file does not exist: " + filePath);
        }
        CSVLoader loader = new CSVLoader();
        loader.setSource(inFile);
        data = loader.getDataSet();
        return data;
    }

    public static void writeInstancesToFile(Instances ins, String outputDir) {
        try {
            CSVSaver s = new CSVSaver();
            s.setFile(new File(outputDir));
            s.setInstances(ins);
            s.setFieldSeparator(",");
            s.writeBatch();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static SimpleKMeans buildKMeans(Instances data) throws Exception {
        SimpleKMeans kmeans = new SimpleKMeans();
        kmeans.setNumClusters(k);
        kmeans.setMaxIterations(maxIteration);
        try {
            kmeans.buildClusterer(data);

        } catch (Exception ex) {
            err.println("Unable to buld Clusterer: " + ex.getMessage());
            ex.printStackTrace();
        }
        return kmeans;
    }


    static public String[] getClassesOfCluster(Instances data_t, int classIndex, int[] classes) {
        Map m = new HashMap();
        int i = 0, j = 0;
        int l = classes.length;
        String cl = null;
        while (i < k) {
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
//    public static void main(String[] args) throws Exception {
//    }
}
