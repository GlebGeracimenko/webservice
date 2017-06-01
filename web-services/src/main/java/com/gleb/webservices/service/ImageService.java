package com.gleb.webservices.service;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import com.gleb.webservices.bo.BOImage;

/**
 * Created by gleb on 26.10.15.
 */
public interface ImageService {

    UUID saveImage(BOImage boImage);

    boolean updateImage(BOImage boImage);

    boolean deleteImage(UUID id);

    BOImage getImageByName(String name);

    Collection<BOImage> getImageByIds(List<UUID> ids);

    BOImage getImageById(UUID id);

    BOImage getImageByInternalLink(String internal_link);

    BOImage getImageByExternalLink(String external_link);

}
