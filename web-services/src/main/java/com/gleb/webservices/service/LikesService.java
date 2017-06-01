package com.gleb.webservices.service;

import java.util.UUID;

public interface LikesService {

    void likeProduct(String productId, UUID userId);

    void dislikeProduct(String productId, UUID userId);
}
