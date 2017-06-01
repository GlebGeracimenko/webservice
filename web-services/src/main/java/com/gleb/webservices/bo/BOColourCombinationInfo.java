package com.gleb.webservices.bo;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class BOColourCombinationInfo {
    private String combinationName;
    private String mainColourName;
    private List<String> combinedColourNames = new ArrayList<String>();

    public String getColourCombinationName() {
        return combinationName;
    }

    public void setColourCombinationName(String colourCombinationName) {
        this.combinationName = colourCombinationName;
    }

    public String getMainColourName() {
        return mainColourName;
    }

    public void setMainColour(String mainColourName) {
        this.mainColourName = mainColourName;
    }

    public List<String> getCombinedColourNames() {
        return combinedColourNames;
    }

    public void setCombinedColourNames(List<String> combinedColourNames) {
        this.combinedColourNames = combinedColourNames;
    }
}
