package com.gleb.webservices.service.impl;

import java.util.Date;
import java.util.UUID;

import com.gleb.webservices.service.LikesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gleb.dao.LikesDAO;

@Service
public class LikesServiceImpl implements LikesService {

    @Autowired
    private LikesDAO likesDAO;

    @Override
    public void likeProduct(String productId, UUID userId) {
        likesDAO.likeProduct(productId, userId, new Date());
    }

    @Override
    public void dislikeProduct(String productId, UUID userId) {
        likesDAO.dislikeProduct(productId, userId, new Date());
    }

}
