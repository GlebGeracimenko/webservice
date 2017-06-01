package com.gleb.dao;

import java.util.Date;
import java.util.UUID;

public interface LikesDAO {

    void likeProduct(String productId, UUID userId, Date time);

    void dislikeProduct(String productId, UUID userId, Date time);

    Long getLikesCountForProduct(String productId);

    Long getDislikesCountForProduct(String productId);
}
