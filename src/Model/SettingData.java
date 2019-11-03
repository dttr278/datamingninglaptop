/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author dttr2
 */
public class SettingData {

    private Map manHinh;
    private Map ram;
    private Map cpu;
    private Map gpu;
    private Map trongLuong;
    private String fileData;
    private Integer k;
    private String fileProduct;

    public String getFileProduct() {
        return fileProduct;
    }

    public void setFileProduct(String fileProduct) {
        this.fileProduct = fileProduct;
    }
    
    public Integer getK() {
        return k;
    }

    public void setK(Integer k) {
        this.k = k;
    }
    public String getFileData() {
        return fileData;
    }

    public void setFileData(String fileData) {
        this.fileData = fileData;
    }

    public Map getManHinh() {
        return manHinh;
    }

    public void setManHinh(Map manHinh) {
        this.manHinh = manHinh;
    }

    public Map getRam() {
        return ram;
    }

    public void setRam(Map ram) {
        this.ram = ram;
    }

    public Map getCpu() {
        return cpu;
    }

    public void setCpu(Map cpu) {
        this.cpu = cpu;
    }

    public Map getGpu() {
        return gpu;
    }

    public void setGpu(Map gpu) {
        this.gpu = gpu;
    }

    public Map getTrongLuong() {
        return trongLuong;
    }

    public void setTrongLuong(Map trongLuong) {
        this.trongLuong = trongLuong;
    }

    public Map getLoaiOCung() {
        return loaiOCung;
    }

    public void setLoaiOCung(Map loaiOCung) {
        this.loaiOCung = loaiOCung;
    }

    public Map getDungLuongOCung() {
        return dungLuongOCung;
    }

    public void setDungLuongOCung(Map dungLuongOCung) {
        this.dungLuongOCung = dungLuongOCung;
    }

    public Map getGia() {
        return gia;
    }

    public void setGia(Map gia) {
        this.gia = gia;
    }
    private Map loaiOCung;
    private Map dungLuongOCung;
    private Map gia;

    public SettingData() {
        manHinh = new HashMap();
        ram = new HashMap();
        cpu = new HashMap();
        gpu = new HashMap();
        trongLuong = new HashMap();
        loaiOCung = new HashMap();
        dungLuongOCung = new HashMap();
        gia = new HashMap();
        
    }
}