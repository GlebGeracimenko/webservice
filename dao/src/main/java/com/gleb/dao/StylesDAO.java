package com.gleb.dao;

import java.util.Collection;

import com.gleb.dao.objects.DBStyle;

public interface StylesDAO {

	String saveStyle(DBStyle dbStyle);

	DBStyle getStyleById(String id);

	DBStyle getStyleByName(String name);

	Collection<DBStyle> getAllStyles();

	Collection<DBStyle> getStyles(Collection<String> ids);
}
