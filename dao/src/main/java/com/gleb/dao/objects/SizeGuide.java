package com.gleb.dao.objects;

import java.util.List;

public class SizeGuide {

    private String store;
    private List<String> ukSizeNumbers;
    private List<String> ukSizeLabels;
    private List<String> bustInches;
    private List<String> bustCms;
    private List<String> waistInches;
    private List<String> waistCms;
    private List<String> hipInches;
    private List<String> hipCms;

    public String getStore() {
        return store;
    }

    public void setStore(String store) {
        this.store = store;
    }

    public List<String> getUkSizeNumbers() {
        return ukSizeNumbers;
    }

    public void setUkSizeNumbers(List<String> ukSizeNumbers) {
        this.ukSizeNumbers = ukSizeNumbers;
    }

    public List<String> getUkSizeLabels() {
        return ukSizeLabels;
    }

    public void setUkSizeLabels(List<String> ukSizeLabels) {
        this.ukSizeLabels = ukSizeLabels;
    }

    public List<String> getBustInches() {
        return bustInches;
    }

    public void setBustInches(List<String> bustInches) {
        this.bustInches = bustInches;
    }

    public List<String> getBustCms() {
        return bustCms;
    }

    public void setBustCms(List<String> bustCms) {
        this.bustCms = bustCms;
    }

    public List<String> getWaistInches() {
        return waistInches;
    }

    public void setWaistInches(List<String> waistInches) {
        this.waistInches = waistInches;
    }

    public List<String> getWaistCms() {
        return waistCms;
    }

    public void setWaistCms(List<String> waistCms) {
        this.waistCms = waistCms;
    }

    public List<String> getHipInches() {
        return hipInches;
    }

    public void setHipInches(List<String> hipInches) {
        this.hipInches = hipInches;
    }

    public List<String> getHipCms() {
        return hipCms;
    }

    public void setHipCms(List<String> hipCms) {
        this.hipCms = hipCms;
    }
}
