package com.gleb.webservices.mapping;

import com.gleb.dao.objects.DBImage;
import com.gleb.webservices.bo.BOImage;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by gleb on 26.10.15.
 */
@Component
public class ImageMapper {

    private Mapper mapper;

    @PostConstruct
    public void init() {
        mapper = new DozerBeanMapper();
    }

    public DBImage map(BOImage boImage) {
        if (boImage == null) {
            return null;
        }
        return mapper.map(boImage, DBImage.class);
    }

    public BOImage map(DBImage dbImage) {
        if (dbImage == null) {
            return null;
        }
        return mapper.map(dbImage, BOImage.class);
    }

    public Collection<BOImage> map(List<DBImage> dbImages) {
        List<BOImage> boImages = new ArrayList<BOImage>();
        for (DBImage image : dbImages) {
            boImages.add(map(image));
        }
        return boImages;
    }

    public Collection<DBImage> mapList(List<BOImage> boImages) {
        List<DBImage> dbImages = new ArrayList<DBImage>();
        for (BOImage image : boImages) {
            dbImages.add(map(image));
        }
        return dbImages;
    }

}
