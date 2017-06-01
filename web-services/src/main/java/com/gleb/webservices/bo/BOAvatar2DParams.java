package com.gleb.webservices.bo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class BOAvatar2DParams {
    private static final Map<String, String> colourMapping = new HashMap<String, String>();
    static {
        colourMapping.put("blonde", "E7F081");
        colourMapping.put("brown", "7A421A");
        colourMapping.put("brunette", "000000");
        colourMapping.put("ginger", "C43623");
        
        colourMapping.put("black", "000000");
        colourMapping.put("dark brown", "7A2E23");
        colourMapping.put("light brown", "9C7348");
        colourMapping.put("asian oriental", "DEBC73");
        colourMapping.put("white", "FFF6BA");
    }

    public static class ColourItem {
        private String id;
        private String hex;

        public ColourItem(String id, String hex) {
            setId(id);
            setHex(hex);
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getHex() {
            return hex;
        }

        public void setHex(String hex) {
            this.hex = hex;
        }
    }

    private List<ColourItem> hairColours = new ArrayList<ColourItem>();
    private List<String> hairSizes;
    private List<String> bodySizes;
    private List<ColourItem> skinColours = new ArrayList<ColourItem>();

    public List<ColourItem> getHairColours() {
        return hairColours;
    }

    public void setHairColours(List<String> hairColoursStr) {
        for (String hairColour : hairColoursStr) {
            String colour = colourMapping.get(hairColour);
            colour = colour == null ? "FFFFFF" : colour;
            hairColours.add(new ColourItem(hairColour, colour));
        }
    }

    public List<String> getHairSizes() {
        return hairSizes;
    }

    public void setHairSizes(List<String> hairSizes) {
        this.hairSizes = hairSizes;
    }

    public List<String> getBodySizes() {
        return bodySizes;
    }

    public void setBodySizes(List<String> bodySizes) {
        this.bodySizes = bodySizes;
    }

    public List<ColourItem> getSkinColours() {
        return skinColours;
    }

    public void setSkinColours(List<String> skinColoursStr) {
        for (String skinColour : skinColoursStr) {
            String colour = colourMapping.get(skinColour);
            colour = colour == null ? "FFFFFF" : colour;
            skinColours.add(new ColourItem(skinColour, colour));
        }
    }
}
