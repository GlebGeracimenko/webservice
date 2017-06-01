package com.gleb.dao;

import java.util.List;

/**
 * Created by gleb on 26.09.15.
 */
public interface MappingDAO {
    public static final String CATEGORY_TYPE = "CATEGORY";
    public static final String TOP_CATEGORY_TYPE = "TOP_CATEGORY";
    public static final String COLOUR_TYPE = "COLOUR";

    public String getInternalName(String externalName, String type);
    
    public List<String> getDistinct(String type);

    public void saveMapping(String externalName, String type, String internalName);
    
    public void deleteMapping(String externalName, String type, String internalName);

}
