package com.gleb.webservices.service.impl;

//import static org.junit.Assert.assertNull;
//import static org.junit.Assert.assertThat;
//import static org.junit.Assert.assertTrue;
//import static org.hamcrest.CoreMatchers.equalTo;
//
//import BasicCQLCassandraDAO;
//import CQLImagesDAO;
//import DBImage;
//import BOImage;
//import ImageMapper;
//import ImageService;
//import org.junit.After;
//
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.ContextHierarchy;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//
//import java.util.ArrayList;
//import java.util.List;

/**
 * Created by gleb on 27.10.15.
 */
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextHierarchy({ @ContextConfiguration("/testApplicationContext.xml") })
public class ImageServiceImplTest {

//    @Autowired
//    private ImageService imageService;
//
//    @Autowired
//    private ImageMapper imageMapper;
//
//    @Autowired
//    private BasicCQLCassandraDAO cassandraDAO;
//
//    public void cleanUp() {
//        cassandraDAO.getSession().execute("truncate " + CQLImagesDAO.IMAGES_TABLE);
//    }
//
//    @After
//    public void after() {
//        cleanUp();
//    }
//
//    @Before
//    public void before() {
//        cleanUp();
//    }
//
//    @Test
//    public void test() {
//        DBImage dbImage = new DBImage();
//        dbImage.setId(1L);
//        dbImage.setInternalName("TestService");
//        dbImage.setInternalLink("TestService");
//        dbImage.setExternalLink("TestService");
//        dbImage.setHoster("TestService");
//
//        imageService.saveImage(imageMapper.map(dbImage));
//
//        BOImage boImage = imageService.getImageById(dbImage.getId());
//        check(dbImage, imageMapper.map(boImage));
//
//        boImage = imageService.getImageByName(dbImage.getInternalName());
//        check(dbImage, imageMapper.map(boImage));
//
//        boImage = imageService.getImageByInternalLink(dbImage.getInternalLink());
//        check(dbImage, imageMapper.map(boImage));
//
//        boImage = imageService.getImageByExternalLink(dbImage.getExternalLink());
//        check(dbImage, imageMapper.map(boImage));
//
//        dbImage.setId(1L);
//        dbImage.setInternalName("TestServiceUpdate");
//        dbImage.setInternalLink("TestServiceUpdate");
//        dbImage.setExternalLink("TestServiceUpdate");
//        dbImage.setHoster("TestServiceUpdate");
//
//        boolean rezult = imageService.updateImage(imageMapper.map(dbImage));
//        assertTrue(rezult);
//
//        boImage = imageService.getImageById(dbImage.getId());
//        check(dbImage, imageMapper.map(boImage));
//
//        DBImage dbImage2 = new DBImage();
//        dbImage2.setId(2L);
//        dbImage2.setInternalName("TestService123");
//        dbImage2.setInternalLink("TestService123");
//        dbImage2.setExternalLink("TestService123");
//        dbImage2.setHoster("TestService123");
//
//        imageService.saveImage(imageMapper.map(dbImage2));
//
//        List<Long> ids = new ArrayList<Long>();
//        ids.add(1L);
//        ids.add(2L);
//
//        List<DBImage> dbImages = (List<DBImage>) imageMapper.mapList((List<BOImage>) imageService.getImageByIds(ids));
//        assertThat(dbImages.size(), equalTo(ids.size()));
//        check(dbImage, dbImages.get(0));
//        check(dbImage2, dbImages.get(1));
//
//        imageService.deleteImage(dbImage2.getId());
//        boImage = imageService.getImageById(dbImage2.getId());
//        assertNull(boImage);
//
//    }
//
//    private void check(DBImage dbImage, DBImage boImage) {
//        assertThat(dbImage.getId(), equalTo(boImage.getId()));
//        assertThat(dbImage.getInternalName(), equalTo(boImage.getInternalName()));
//        assertThat(dbImage.getInternalLink(), equalTo(boImage.getInternalLink()));
//        assertThat(dbImage.getExternalLink(), equalTo(boImage.getExternalLink()));
//        assertThat(dbImage.getHoster(), equalTo(boImage.getHoster()));
//    }

}
