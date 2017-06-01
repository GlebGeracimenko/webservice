package com.gleb.webservices.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.gleb.webservices.service.Avatar2DService;

@Service
public class Avatar2DServiceImpl implements Avatar2DService {
    private static final Map<String, String> avatarSizeMapping = new HashMap<String, String>();
    private static final Map<String, String> avatarHairSizeMapping = new HashMap<String, String>();
    private static final Map<String, String> avatarHairColourMapping = new HashMap<String, String>();
    private static final Map<String, String> avatarSkinColourMapping = new HashMap<String, String>();

    @Value("${services.avatar2d.avatar.baseUrl}")
    private String avatar2DbaseUrl;

    static {
        avatarSizeMapping.put("xs", "6");
        avatarSizeMapping.put("s", "8");
        avatarSizeMapping.put("m", "10");
        avatarSizeMapping.put("l", "12");
        avatarSizeMapping.put("xl", "14");

        avatarSkinColourMapping.put("black", "1");
        avatarSkinColourMapping.put("dark brown", "2");
        avatarSkinColourMapping.put("light brown", "3");
        avatarSkinColourMapping.put("asian oriental", "0");
        avatarSkinColourMapping.put("white", "4");

        avatarHairSizeMapping.put("long", "1");
        avatarHairSizeMapping.put("short", "0");

        avatarHairColourMapping.put("blonde", "0");
        avatarHairColourMapping.put("brown", "3");
        avatarHairColourMapping.put("brunette", "1");
        avatarHairColourMapping.put("ginger", "2");

    }

    @Override
    public List<String> getAvailableSkinColours() {
        List<String> hairColours = new ArrayList<String>();
        hairColours.add("white");
        hairColours.add("asian oriental");
        hairColours.add("light brown");
        hairColours.add("dark brown");
        hairColours.add("black");
        return hairColours;// new ArrayList<String>(avatarSkinColourMapping.keySet());
    }

    @Override
    public List<String> getAvailableHairColours() {
        List<String> hairColours = new ArrayList<String>();
        hairColours.add("blonde");
        hairColours.add("ginger");
        hairColours.add("brown");
        hairColours.add("brunette");
        return hairColours; //new ArrayList<String>(avatarHairColourMapping.keySet());
    }

    @Override
    public List<String> getAvailableAvatarSizes() {
        List<String> sizes = new ArrayList<String>();
        sizes.add("xs");
        sizes.add("s");
        sizes.add("m");
        sizes.add("l");
        sizes.add("xl");
        return sizes;
    }

    @Override
    public List<String> getAvailableHairSizes() {
        return new ArrayList<String>(avatarHairSizeMapping.keySet());
    }

    @Override
    public String constructLink(String skinColour, String hairColour, String bodySize, String hairSize, String topCode, String bottomCode, String shoesCode, String bagCode) {
        hairSize = avatarHairSizeMapping.get(hairSize);
        bodySize = avatarSizeMapping.get(bodySize);
        skinColour = avatarSkinColourMapping.get(skinColour);
        hairColour = avatarHairColourMapping.get(hairColour);
        if (hairSize == null || bodySize == null || skinColour == null || hairColour == null) {
            return null;
        }
        StringBuilder builder = new StringBuilder();
        builder.append(avatar2DbaseUrl);
        builder.append(bodySize);
        builder.append("_");
        builder.append(skinColour);
        builder.append("_");
        builder.append(hairColour);
        builder.append("_");
        builder.append(hairSize);
        builder.append("_");
        builder.append(convertCode(topCode));
        builder.append("_");
        builder.append(convertCode(bottomCode));
        builder.append("_");
        builder.append(convertCode(shoesCode));
        builder.append("_");
        builder.append(convertCode(bagCode));
        builder.append(".jpg");
        return builder.toString();
    }

    private String convertCode(String code) {
        if (code == null || code.trim().equals("")) {
            return "0000";
        }
        return code.trim();
    }
}
