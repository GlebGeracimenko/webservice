package com.gleb.webservices.service.impl;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.gleb.webservices.bo.BOStyle;
import com.gleb.webservices.service.StyleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gleb.dao.FollowsDAO;
import com.gleb.dao.StylesDAO;
import com.gleb.dao.objects.DBStyle;
import com.gleb.dao.objects.DBUser;
import com.gleb.webservices.mapping.StylesMapper;
import com.gleb.webservices.service.IdsService;

@Service
public class StyleServiceImpl implements StyleService {

    @Autowired
    private StylesDAO stylesDAO;

    @Autowired
    private StylesMapper styleMapper;

    @Autowired
    private FollowsDAO followsDAO;

    @Autowired
    private IdsService idsService;

    @Override
    public String saveStyle(BOStyle boStyle) {
        if (boStyle.getId() == null) {
            boStyle.setId(idsService.getNewIdAsString());
        }
        DBStyle dbStyle = styleMapper.map(boStyle);
        return stylesDAO.saveStyle(dbStyle);
    }

    @Override
    public BOStyle getStyle(String id) {
        DBStyle dbStyle = stylesDAO.getStyleById(id);
        return styleMapper.map(dbStyle);
    }

    @Override
    public Collection<BOStyle> getAll() {
        Collection<DBStyle> dbStyles = stylesDAO.getAllStyles();
        return styleMapper.map(dbStyles);
    }

    @Override
    public void followStyle(DBUser dbUser, String styleId) {
        followsDAO.follow(dbUser.getId(), styleId, FollowsDAO.TYPE_STYLE);
    }

    @Override
    public void unfollowStyle(DBUser dbUser, String styleId) {
        followsDAO.unfollow(dbUser.getId(), styleId, FollowsDAO.TYPE_STYLE);
    }

    @Override
    public Collection<BOStyle> followingStyles(DBUser dbUser) {
        Collection<String> styleIds = followsDAO.getAllFollowedObjectsByUser(dbUser.getId(), FollowsDAO.TYPE_STYLE);
        Set<String> ids = new HashSet<String>();
        for (String id : styleIds) {
            ids.add(id);
        }
        Collection<DBStyle> dbStyles = stylesDAO.getStyles(ids);
        return styleMapper.map(dbStyles);
    }
}
