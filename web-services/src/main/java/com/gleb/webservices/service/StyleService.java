package com.gleb.webservices.service;

import java.util.Collection;

import com.gleb.dao.objects.DBUser;
import com.gleb.webservices.bo.BOStyle;

public interface StyleService {

	String saveStyle(BOStyle boStyle);

	BOStyle getStyle(String id);

	Collection<BOStyle> getAll();

	void followStyle(DBUser dbUser, String styleId);

	void unfollowStyle(DBUser dbUser, String styleId);

	Collection<BOStyle> followingStyles(DBUser dbUser);
}
