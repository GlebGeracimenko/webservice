package com.gleb.dao;

import java.util.List;

import com.gleb.dao.objects.DBColour;

/**
 * Created by gleb on 16.09.15.
 */
public interface ColourDAO {

    public static final String COLOUR_TYPE = "colours";

    public String save(DBColour dbColor);

    public List<DBColour> getAll();

    public DBColour getById(String id);

    public DBColour getByName(String nameColor);

}
