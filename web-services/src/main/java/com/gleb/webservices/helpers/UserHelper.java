package com.gleb.webservices.helpers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.gleb.dao.objects.DBUser;

public class UserHelper {

	public static List<GrantedAuthority> getAuthorities(DBUser user) {
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		List<String> groups = user.getGroups();
		if (groups == null) {
			return authorities;
		}
		for (String group : groups) {
			authorities.add(new SimpleGrantedAuthority(group));
		}
		return authorities;
	}

	// TODO:Move here code from userService fb login
	public static DBUser mapDBUserFromFacebookAnswer(Map<String, Object> answer) {
		return null;
	}
}
