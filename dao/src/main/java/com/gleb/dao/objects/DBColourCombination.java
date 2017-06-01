package com.gleb.dao.objects;

import java.util.ArrayList;
import java.util.List;

public class DBColourCombination {
    public static final String IDENTIFIER = "DBColourCombination";
    private String id;
    private String mainColourName;
    private String secondColourName;
    private String combinationName;
    private List<String> combinedColourIds = new ArrayList<>();

    public String getCombinationName() {
        return combinationName;
    }

    public void setCombinationName(String combinationName) {
        this.combinationName = combinationName;
    }

    public String getMainColourName() {
        return mainColourName;
    }

    public void setMainColourName(String mainColourName) {
        this.mainColourName = mainColourName;
    }

    public List<String> getCombinedColourIds() {
        return combinedColourIds;
    }

    public void setCombinedColourIds(List<String> combinedColourIds) {
        this.combinedColourIds = combinedColourIds;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSecondColourName() {
        return secondColourName;
    }

    public void setSecondColourName(String secondColourName) {
        this.secondColourName = secondColourName;
    }
}