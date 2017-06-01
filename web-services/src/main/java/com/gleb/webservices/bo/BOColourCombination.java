package com.gleb.webservices.bo;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class BOColourCombination {
    private String mainColourName;
    private String secondColourName;
    private String combinationName;
    private String id;
    private List<String> combinedColourIds = new ArrayList<>();

    public String getMainColourName() {
        return mainColourName;
    }

    public String getId() {
        return id;
    }

    public void setMainColourName(String mainColourName) {
        this.mainColourName = mainColourName;
    }

    public String getSecondColourName() {
        return secondColourName;
    }

    public void setSecondColourName(String secondColourName) {
        this.secondColourName = secondColourName;
    }

    public String getCombinationName() {
        return combinationName;
    }

    public void setCombinationName(String combinationName) {
        this.combinationName = combinationName;
    }

    public List<String> getCombinedColourIds() {
        return combinedColourIds;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setCombinedColourIds(List<String> combinedColourIds) {
        this.combinedColourIds = combinedColourIds;
    }

}