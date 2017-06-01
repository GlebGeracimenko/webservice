package com.gleb.dao.cassandra;

import static com.datastax.driver.core.querybuilder.QueryBuilder.eq;
import static com.datastax.driver.core.querybuilder.QueryBuilder.in;
import static com.datastax.driver.core.querybuilder.QueryBuilder.select;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.querybuilder.Select;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import com.gleb.dao.ImagesDAO;
import com.gleb.dao.objects.DBImage;

/**
 * Created by gleb on 25.10.15.
 */
@Repository
public class CQLImagesDAO implements ImagesDAO {

    public static final String IMAGES_TABLE = "images";

    @Autowired
    private BasicCQLCassandraDAO basicCassandraDAO;

    private Mapper<DBImage> imageMapper;

    @PostConstruct
    public void init() {
        MappingManager manager = new MappingManager(basicCassandraDAO.getSession());
        imageMapper = manager.mapper(DBImage.class);
    }

    @Override
    public UUID saveImage(DBImage dbImage) {
        imageMapper.save(dbImage);
        return dbImage.getId();
    }

    @Override
    public boolean updateImage(DBImage dbImage) {
        saveImage(dbImage);
        return true;
    }

    @Override
    public boolean deleteImage(UUID id) {
        imageMapper.delete(id);
        return true;
    }

    @Override
    public DBImage getImageByName(String name) {
        Select.Where select = select().all().from(IMAGES_TABLE).where(eq("internal_name", name));
        ResultSet resultSet = basicCassandraDAO.getSession().execute(select);
        if (resultSet.getAvailableWithoutFetching() < 1) {
            return null;
        }
        return imageMapper.map(resultSet).one();
    }

    @Override
    public Collection<DBImage> getImageByIds(List<UUID> ids) {
        Select.Where select = select().all().from(IMAGES_TABLE).where(in("id", ids));
        ResultSet resultSet = basicCassandraDAO.getSession().execute(select);
        if (resultSet.getAvailableWithoutFetching() < 1) {
            return null;
        }
        return imageMapper.map(resultSet).all();
    }

    @Override
    public DBImage getImageById(UUID id) {
        Select.Where select = select().all().from(IMAGES_TABLE).where(eq("id", id));
        ResultSet resultSet = basicCassandraDAO.getSession().execute(select);
        if (resultSet.getAvailableWithoutFetching() < 1) {
            return null;
        }
        return imageMapper.map(resultSet).one();
    }

    @Override
    public DBImage getImageByInternalLink(String internal_link) {
        Select.Where select = select().all().from(IMAGES_TABLE).where(eq("internal_link", internal_link));
        ResultSet resultSet = basicCassandraDAO.getSession().execute(select);
        if (resultSet.getAvailableWithoutFetching() < 1) {
            return null;
        }
        return imageMapper.map(resultSet).one();
    }

    @Override
    public DBImage getImageByExternalLink(String external_link) {
        Select.Where select = select().all().from(IMAGES_TABLE).where(eq("external_link", external_link));
        ResultSet resultSet = basicCassandraDAO.getSession().execute(select);
        if (resultSet.getAvailableWithoutFetching() < 1) {
            return null;
        }
        return imageMapper.map(resultSet).one();
    }

}
