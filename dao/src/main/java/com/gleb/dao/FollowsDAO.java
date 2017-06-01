package com.gleb.dao;

import java.util.Collection;
import java.util.UUID;

public interface FollowsDAO {

	public static final String TYPE_BRAND = "BRAND";
	public static final String TYPE_STYLE = "STYLE";

    void follow(UUID userId, String objectId, String type);
    
    boolean checkFollow(UUID userId, String objectId, String type);

    void unfollow(UUID userId, String objectId, String type);

    Collection<String> getAllFollowedObjectsByUser(UUID userId, String type);

    Collection<String> getAllUserFollowingObject(String brandId, String type);
}
