package com.gleb.webservices.service.impl;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import com.gleb.webservices.mapping.ImageMapper;
import com.gleb.webservices.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gleb.dao.cassandra.CQLImagesDAO;
import com.gleb.dao.objects.DBImage;
import com.gleb.webservices.bo.BOImage;
import com.gleb.webservices.service.IdsService;

/**
 * Created by gleb on 26.10.15.
 */
@Service
public class ImageServiceImpl implements ImageService {

    @Autowired
    private ImageMapper imageMapper;

    @Autowired
    private CQLImagesDAO cqlImagesDAO;

    @Autowired
    private IdsService idsService;

    @Override
    public UUID saveImage(BOImage boImage) {
        DBImage dbImage = imageMapper.map(boImage);
        if (dbImage.getId() == null) {
            dbImage.setId(idsService.getNewId());
        }
        return cqlImagesDAO.saveImage(dbImage);
    }

    @Override
    public boolean updateImage(BOImage boImage) {
        DBImage dbImage = imageMapper.map(boImage);
        if (dbImage.getId() == null) {
            dbImage.setId(idsService.getNewId());
        }
        return cqlImagesDAO.updateImage(dbImage);
    }

    @Override
    public boolean deleteImage(UUID id) {
        return cqlImagesDAO.deleteImage(id);
    }

    @Override
    public BOImage getImageByName(String name) {
        DBImage dbImage = cqlImagesDAO.getImageByName(name);
        if(dbImage == null) {
            return null;
        }
        return imageMapper.map(dbImage);
    }

    @Override
    public Collection<BOImage> getImageByIds(List<UUID> ids) {
        List<DBImage> dbImages = (List<DBImage>) cqlImagesDAO.getImageByIds(ids);
        return imageMapper.map(dbImages);
    }

    @Override
    public BOImage getImageById(UUID id) {
        DBImage dbImage = cqlImagesDAO.getImageById(id);
        return imageMapper.map(dbImage);
    }

    @Override
    public BOImage getImageByInternalLink(String internalLink) {
        DBImage dbImage = cqlImagesDAO.getImageByInternalLink(internalLink);
        return imageMapper.map(dbImage);
    }

    @Override
    public BOImage getImageByExternalLink(String externalLink) {
        DBImage dbImage = cqlImagesDAO.getImageByExternalLink(externalLink);
        return imageMapper.map(dbImage);
    }

}
