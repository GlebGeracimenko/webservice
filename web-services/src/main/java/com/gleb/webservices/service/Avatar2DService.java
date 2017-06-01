package com.gleb.webservices.service;

import java.util.List;

public interface Avatar2DService {
	List<String> getAvailableSkinColours();

	List<String> getAvailableHairColours();

	List<String> getAvailableAvatarSizes();

	List<String> getAvailableHairSizes();

	String constructLink(String skinColour, String hairColour, String bodySize, String hairSize, String topCode, String bottomCode, String shoesCode, String bagCode);
}
