package com.gleb.webservices.bo;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.gleb.dao.objects.Colour;

/**
 * Created by gleb on 16.09.15.
 */
@XmlRootElement
public class BOColour {
    private String id;
    private String name;
    private List<String> colours;
    private Colour exampleColour;
    private String group;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getColours() {
        return colours;
    }

    public void setColours(List<String> colours) {
        this.colours = colours;
    }

    public Colour getExampleColour() {
        return exampleColour;
    }

    public void setExampleColour(Colour exampleColour) {
        this.exampleColour = exampleColour;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }
}