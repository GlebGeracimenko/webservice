package com.gleb.dao;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import com.gleb.dao.objects.DBImage;

/**
 * Created by gleb on 25.10.15.
 */
public interface ImagesDAO {

    UUID saveImage(DBImage dbImage);

    boolean updateImage(DBImage dbImage);

    boolean deleteImage(UUID id);

    DBImage getImageByName(String name);

    Collection<DBImage> getImageByIds(List<UUID> ids);

    DBImage getImageById(UUID id);

    DBImage getImageByInternalLink(String internal_link);

    DBImage getImageByExternalLink(String external_link);

}
