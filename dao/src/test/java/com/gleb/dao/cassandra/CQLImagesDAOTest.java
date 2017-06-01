package com.gleb.dao.cassandra;

import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;

import com.gleb.dao.objects.DBImage;
import com.gleb.dao.ImagesDAO;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by gleb on 25.10.15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextHierarchy({ @ContextConfiguration("/testApplicationContext.xml") })
@Ignore
public class CQLImagesDAOTest {

    @Autowired
    private ImagesDAO imagesDAO;

    @Autowired
    private BasicCQLCassandraDAO basicDAO;

    public void cleanUp() {
        basicDAO.getSession().execute("truncate " + CQLLikesDAO.LIKES_COLUMN_FAMILY);
    }

    // @Before
    public void before() {
        cleanUp();
    }

    @Test
    public void test() {
        DBImage dbImage = new DBImage();
        dbImage.setId(UUID.randomUUID());
        dbImage.setInternalName("TEST");
        dbImage.setInternalLink("TEST");
        dbImage.setExternalLink("TEST");
        dbImage.setHoster("TEST");

        imagesDAO.saveImage(dbImage);
        DBImage dbImageGetId = imagesDAO.getImageById(dbImage.getId());

        check(dbImage, dbImageGetId);

        dbImage.setInternalName("TEST1");
        dbImage.setInternalLink("TEST1");
        dbImage.setExternalLink("TEST1");
        dbImage.setHoster("TEST1");

        imagesDAO.updateImage(dbImage);
        DBImage dbImageGetByName = imagesDAO.getImageByName(dbImage.getInternalName());

        check(dbImage, dbImageGetByName);

        DBImage dbImageGetByInterLink = imagesDAO.getImageByInternalLink(dbImage.getInternalLink());

        check(dbImage, dbImageGetByInterLink);

        DBImage dbImageGetByExterLink = imagesDAO.getImageByExternalLink(dbImage.getExternalLink());

        check(dbImage, dbImageGetByExterLink);

        DBImage dbImage2 = new DBImage();
        dbImage2.setId(UUID.randomUUID());
        dbImage2.setInternalName("TEST1");
        dbImage2.setInternalLink("TEST1");
        dbImage2.setExternalLink("TEST1");
        dbImage2.setHoster("TEST1");

        imagesDAO.saveImage(dbImage2);

        List<UUID> ids = new ArrayList<UUID>();
        ids.add(dbImage.getId());
        ids.add(dbImage2.getId());

        List<DBImage> dbImages = (List<DBImage>) imagesDAO.getImageByIds(ids);

        check(dbImage, dbImages.get(0));
        check(dbImage2, dbImages.get(1));

        imagesDAO.deleteImage(dbImage2.getId());
        DBImage dbImageDelete = imagesDAO.getImageById(dbImage2.getId());
        Assert.assertNull(dbImageDelete);

    }

    private void check(DBImage dbImage, DBImage dbImage1) {
        assertThat(dbImage.getId(), equalTo(dbImage1.getId()));
        assertThat(dbImage.getInternalName(), equalTo(dbImage1.getInternalName()));
        assertThat(dbImage.getInternalLink(), equalTo(dbImage1.getInternalLink()));
        assertThat(dbImage.getExternalLink(), equalTo(dbImage1.getExternalLink()));
        assertThat(dbImage.getHoster(), equalTo(dbImage1.getHoster()));
    }

}
